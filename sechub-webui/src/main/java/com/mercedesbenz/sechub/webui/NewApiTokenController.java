// SPDX-License-Identifier: MIT
package com.mercedesbenz.sechub.webui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mercedesbenz.sechub.webui.configuration.SecHubAccessService;

@Controller
public class NewApiTokenController {
    @Autowired
    NewApiTokenService newApiTokenService;

    @Autowired
    SecHubAccessService accessService;

    @GetMapping("/requestNewApiToken")
    String requestNewApiToken(Model model) {
        // TODO Jeremias Eppler, 2023-09-27: Replace with real user email once proper authentication is added
        String email = "email@example.org";

        newApiTokenService.requestNewApiToken(email);
        model.addAttribute("sechubServerUrl", accessService.getSecHubServerUri());
        model.addAttribute("userEmail", email);

        return "newApiToken";
    }
}
