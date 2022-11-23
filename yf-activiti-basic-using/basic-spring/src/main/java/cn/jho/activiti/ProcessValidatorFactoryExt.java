package cn.jho.activiti;

import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.activiti.validation.validator.ValidatorSet;
import org.activiti.validation.validator.ValidatorSetNames;

/**
 * ProcessValidatorFactoryExt
 *
 * @author JHO xu-jihong@qq.com
 */
public class ProcessValidatorFactoryExt extends ProcessValidatorFactory {

    @Override
    public ProcessValidator createDefaultProcessValidator() {
        ProcessValidator validator = super.createDefaultProcessValidator();
        ValidatorSet validatorSet = new ValidatorSet(ValidatorSetNames.ACTIVITI_EXECUTABLE_PROCESS);
        validatorSet.addValidator(new CustomUserTaskValidator());
        validator.getValidatorSets().add(validatorSet);
        return validator;
    }

}
