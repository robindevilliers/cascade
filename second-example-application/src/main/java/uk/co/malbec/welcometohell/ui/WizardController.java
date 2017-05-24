package uk.co.malbec.welcometohell.ui;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.co.malbec.welcometohell.wizard.WizardEngine;
import uk.co.malbec.welcometohell.wizard.domain.View;
import uk.co.malbec.welcometohell.wizard.domain.Wizard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class WizardController {

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private WizardEngine wizardEngine;

    @Autowired
    private RenderingEngine renderingEngine;

    @Autowired
    private HttpSession httpSession;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView handle(HttpServletResponse response)
            throws IOException {

        String wizardSessionId = wizardEngine.start("questionnaire");
        String pageId = wizardEngine.getLastPageId(wizardSessionId);
        httpSession.setAttribute("wizardSessionId", wizardSessionId);

        return renderView(wizardSessionId, pageId, response);
    }


    @RequestMapping(value = "/{pageId}", method = RequestMethod.GET)
    public ModelAndView handleGet(@PathVariable String pageId, HttpServletResponse response)
            throws IOException {

        String wizardSessionId = (String) httpSession.getAttribute("wizardSessionId");

        return renderView(wizardSessionId, pageId, response);
    }

    private ModelAndView renderView(String wizardSessionId, String pageId, HttpServletResponse response) throws IOException {


        Map<String, Object> model = new HashMap<>();

        Wizard wizard = wizardEngine.getCurrentWizard(wizardSessionId);

        if (wizard.getRem() != null) {
            model.put("fontSize", wizard.getRem() + "rem");
        }

        if (wizard.getRem() != null) {
            model.put("fontSize", wizard.getRem() + "em");
        }

        View view = wizardEngine.getView(wizardSessionId, pageId);

        if (view.getRem() != null) {
            model.put("fontSize", view.getRem() + "rem");
        }

        if (view.getRem() != null) {
            model.put("fontSize", view.getRem() + "em");
        }

        Map<String, Object> data = wizardEngine.getData(wizardSessionId);

        model.put("content", renderingEngine.renderElementComposer(wizardSessionId, pageId, view, data));
        model.put("pageId", pageId);
        model.put("title", view.getTitle());

        return new ModelAndView("main", model);
    }

    @RequestMapping(value = "/{pageId}", method = RequestMethod.POST)
    public String handlePost(@PathVariable String pageId, HttpServletRequest request) throws IOException {

        String wizardSessionId = (String) httpSession.getAttribute("wizardSessionId");
        if (wizardEngine.getCurrentWizard(wizardSessionId) == null){
            return "redirect:/";
        }

        Map<String, String> input = request.getParameterMap()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()[0]));

        Map<String, Object> data = wizardEngine.validateData(wizardSessionId, pageId, input);

        //the client can now add data to this submission which will form part of the image.
        wizardEngine.submitData(wizardSessionId, pageId, data);

        wizardEngine.route(wizardSessionId, pageId);

        String externalName = wizardEngine.getLastViewExternalName(wizardSessionId);
        if (externalName == null){
            return "redirect:/" + wizardEngine.getLastPageId(wizardSessionId);
        } else if ("final".equals(externalName)) {
            return "redirect:/final";
        } else if ("escape".equals(externalName)) {
            return "redirect:/escape";
        } else {
            throw new RuntimeException("Routing error");
        }

    }

    @RequestMapping(value = "/link/{pageId}/{viewId}", method = RequestMethod.GET)
    public String handleLink(@PathVariable String pageId, @PathVariable String viewId) throws IOException {

        String wizardSessionId = (String) httpSession.getAttribute("wizardSessionId");

        //TODO - find out why '.page' isn't coming through
        wizardEngine.setNextView(wizardSessionId, pageId, viewId + ".page");

        String externalName = wizardEngine.getLastViewExternalName(wizardSessionId);
        if (externalName == null){
            return "redirect:/" + wizardEngine.getLastPageId(wizardSessionId);
        }  else if ("final".equals(externalName)) {
            return "redirect:/final";
        } else {
            throw new RuntimeException("Routing error");
        }
    }


}
