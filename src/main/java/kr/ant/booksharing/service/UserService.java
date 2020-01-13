package kr.ant.booksharing.service;

import kr.ant.booksharing.dao.UserMapper;
import kr.ant.booksharing.domain.User;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.SignIn.SignInRes;
import kr.ant.booksharing.model.SignIn.SignInReq;
import kr.ant.booksharing.model.SignUp.SignUpReq;
import kr.ant.booksharing.repository.UserRepository;
import kr.ant.booksharing.utils.ResponseMessage;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(final UserMapper userMapper, final PasswordEncoder passwordEncoder,
                       final UserRepository userRepository, final JwtService jwtService) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }


    /**
     * 회원 정보 저장
     *
     * @param user 회원
     * @return DefaultRes
     */
    public DefaultRes saveUser(final User user) {
        try {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            System.out.println(user.toString());
            userRepository.save(user);
            return DefaultRes.res(StatusCode.CREATED, "회원 정보 저장 성공");
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "회원 정보 저장 실패");
        }
    }

    /**
     * 회원 정보 인증
     *
     * @param signInReq 회원 데이터
     * @return DefaultRes
     */
    public DefaultRes authUser(final SignInReq signInReq) {
        try {
            if(userMapper.findUser(signInReq) != null){
                SignInRes signInRes = userMapper.findUser(signInReq);
                if(passwordEncoder.matches(signInReq.getPassword(), signInRes.getPassword())){
                    final JwtService.TokenRes tokenRes =
                            new JwtService.TokenRes(jwtService.create(signInRes.getId()));

                    signInRes.setTokenRes(tokenRes); signInRes.setPassword("");

                    return DefaultRes.res(StatusCode.CREATED, ResponseMessage.LOGIN_SUCCESS,signInRes);
                }
                else { return DefaultRes.res(StatusCode.FORBIDDEN, ResponseMessage.LOGIN_FAIL); }
            }
            else{
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }


        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
    /**
     * 이메일 중복 검사
     *
     * @param email 회원 이메일
     * @return DefaultRes
     */
    public DefaultRes checkEmail(final String email) {
        try {
            if(userMapper.checkEmail(email) != null && !(userMapper.checkEmail(email).equals(""))){
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.ALREADY_USER);
            }
            else{
                return DefaultRes.res(StatusCode.OK, ResponseMessage.USABLE_USER);
            }
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 회원 비밀번호 변경
     *
     * @param signInReq 회원 이메일
     * @return DefaultRes
     */
    public DefaultRes modifyPassword(final SignInReq signInReq) {
        try {
            userMapper.changeUserPassword(signInReq.getEmail(), passwordEncoder.encode(signInReq.getPassword()));
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CHANGED_PWD);
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
