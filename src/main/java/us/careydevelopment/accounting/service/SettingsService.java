package us.careydevelopment.accounting.service;

import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import us.careydevelopment.accounting.exception.NotFoundException;
import us.careydevelopment.accounting.exception.ServiceException;
import us.careydevelopment.accounting.model.Settings;
import us.careydevelopment.accounting.repository.SettingsRepository;

import java.util.Optional;

@Component
public class SettingsService extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(SettingsService.class);

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private SettingsValidationService settingsValidationService;

    public Settings currentSettings() {
        final Settings settings = settingsRepository.findByOwnerUsername(sessionUtil.getCurrentUser().getUsername());

        if (settings == null) {
            throw new ServiceException("Could not retrieve settings for user!");
        }

        return settings;
    }

    public Settings retrieve(final String id) {
        final Optional<Settings> settingsOptional = settingsRepository.findById(id);

        if (settingsOptional.isEmpty()) throw new NotFoundException("Settings with ID " + id + " doesn't exist");

        return settingsOptional.get();
    }

    @Transactional
    public Settings create(final Settings settings, final BindingResult bindingResult) {
        settingsValidationService.validateNew(settings, bindingResult);

        sanitizeData(settings);

        final Settings returnedSettings = settingsRepository.save(settings);
        return returnedSettings;
    }

    @Transactional
    public Settings update(final Settings settings, final BindingResult bindingResult) {
        //accountValidationService.validateExisting(account, bindingResult);

        final Settings returnedSettings = settingsRepository.save(settings);
        return returnedSettings;
    }

    public void update(final Settings settings) {
        //accountValidationService.validate(settings);
        settingsRepository.save(settings);
    }

    @VisibleForTesting
    void sanitizeData(final Settings settings) {
        setOwner(settings);
    }
}
