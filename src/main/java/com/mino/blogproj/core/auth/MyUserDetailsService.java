package com.mino.blogproj.core.auth;

import com.mino.blogproj.model.user.User;
import com.mino.blogproj.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    //시큐리티를 커스터마이징했기 때문에
    //UsernamePasswordAuthenticationFilter가 loadUserByUsername 발동시키도록
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User userPS = userRepository.findByUsername(username).orElseThrow();
        User userPS = userRepository.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException("Bad Credential")
        );
        //커스텀 Exception를 날리면 안된다.
        //: UsernameNotFoundException 발동해야 failureHandler가 처리함

        return new MyUserDetails(userPS);
        //MyUserDetails(userPS)이 Authentication에 담긴다.
    }
}
