package us.careydevelopment.accounting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import us.careydevelopment.accounting.model.Settings;
import us.careydevelopment.accounting.service.SettingsService;
import us.careydevelopment.accounting.util.SessionUtil;
import us.careydevelopment.util.api.model.IRestResponse;
import us.careydevelopment.util.api.response.ResponseEntityUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
public class SettingsController {
	
    private static final Logger LOG = LoggerFactory.getLogger(SettingsController.class);

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private SettingsService settingsService;

    @PostMapping("/settings")
    public ResponseEntity<IRestResponse<Settings>> postTransaction(final HttpServletRequest request,
                                                                  @Valid @RequestBody final Settings settings,
                                                                  final BindingResult bindingResult) {
        LOG.debug("Creating settings: " + settings);

        final Settings returnedSettings = settingsService.create(settings, bindingResult);

        return ResponseEntityUtil.createSuccessfulResponseEntity("Successfully created settings!",
                HttpStatus.CREATED.value(),
                returnedSettings);
    }
}
