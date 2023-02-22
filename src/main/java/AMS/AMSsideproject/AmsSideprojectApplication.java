package AMS.AMSsideproject;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.web.security.PrincipalDetails;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@SpringBootApplication
public class AmsSideprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmsSideprojectApplication.class, args);
	}




	//!!!Test!!!
	//Spring Data Jpa - Auditing(@CreatedBy, @LastModifiedBy) 사용
	public class AuditorAwareImpl implements AuditorAware<String> {
		@Override
		public Optional<String> getCurrentAuditor() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String name = "";

			if(authentication!=null) {
				//nickname
//				PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
//				User user = principal.getUser()
//				name = user.getNickname();

				//email
				name = authentication.getName();
			}
			return Optional.of(name);
		}
	}
	@Bean
	public AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}

}
