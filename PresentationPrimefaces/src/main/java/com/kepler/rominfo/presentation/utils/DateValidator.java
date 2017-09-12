package com.kepler.rominfo.presentation.utils;

import com.kepler.rominfo.service.services.CourseService;
import org.primefaces.context.RequestContext;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@FacesValidator("dateValidator")
public class DateValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date universityStartDate = null;
        Date univeristyEndDate = null;
        try {
            universityStartDate = sdf.parse(CourseService.BEGINNING_OF_SCHOOL);
            univeristyEndDate = sdf.parse(CourseService.END_OF_SCHOOL);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        Date date = (Date)o;
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        /* dayOfWeek=7 => SATURDAY
         dayOfWeek=1 => SUNDAY*/
        if(date.before(universityStartDate) || date.after(univeristyEndDate) || dayOfWeek == 1 || dayOfWeek == 7) {
            FacesMessage msg =
                    new FacesMessage("Invalid Date","Invalid Date");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }else {
            RequestContext.getCurrentInstance().execute("PF('eventDialog').hide()");
        }
    }
}
