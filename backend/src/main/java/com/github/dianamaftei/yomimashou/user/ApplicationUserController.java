package com.github.dianamaftei.yomimashou.user;

import com.github.dianamaftei.yomimashou.text.ProgressStatus;
import java.security.Principal;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class ApplicationUserController {

  private final ApplicationUserService applicationUserService;

  @Autowired
  public ApplicationUserController(ApplicationUserService applicationUserService) {
    this.applicationUserService = applicationUserService;
  }

  @PostMapping("/register")
  public void register(@Valid @RequestBody ApplicationUser user) {
    applicationUserService.register(user);
  }

  @GetMapping("/afterLogout")
  public String afterLogout() {
    return "logged out";
  }

  @PostMapping("/textStatus")
  public void setTextStatus(Principal principal, @RequestParam Long textId,
      @RequestParam ProgressStatus progressStatus) {
    applicationUserService.setTextStatus(principal.getName(), textId, progressStatus);
  }

  @GetMapping("/textStatus")
  public Map<Long, ProgressStatus> getTextStatuses(Principal principal) {
    return applicationUserService.getTextStatuses(principal.getName());
  }

}
