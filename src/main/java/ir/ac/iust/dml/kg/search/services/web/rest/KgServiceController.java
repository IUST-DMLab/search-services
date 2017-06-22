package ir.ac.iust.dml.kg.search.services.web.rest;

import io.swagger.annotations.Api;
import ir.ac.iust.dml.kg.search.logic.kgservice.KgServiceLogic;
import ir.ac.iust.dml.kg.search.logic.kgservice.data.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kgservice")
@Api(tags = "kgservice", description = "سرویس‌های درخواست شده از مرکز تحقیقات")
public class KgServiceController {

    final private KgServiceLogic kgServiceLogic = new KgServiceLogic();

    @RequestMapping(value = "/struct/getparent", method = RequestMethod.GET)
    @ResponseBody
    ParentNode getParent(@RequestParam String childUrl) {
        return kgServiceLogic.getParent(childUrl);
    }

    @RequestMapping(value = "/struct/getchilds", method = RequestMethod.GET)
    @ResponseBody
    ChildNodes getChildren(@RequestParam String parentUrl) {
        return kgServiceLogic.getChildren(parentUrl);
    }

    @RequestMapping(value = "/struct/getinfo", method = RequestMethod.GET)
    @ResponseBody
    ClassInfo getClassInfo(@RequestParam String url) {
        return kgServiceLogic.getClassInfo(url);
    }

    @RequestMapping(value = "/content/getentityinfo", method = RequestMethod.GET)
    @ResponseBody
    EntityData getEntityInfo(@RequestParam String url) {
        return kgServiceLogic.getEntityInfo(url);
    }

    @RequestMapping(value = "/content/getentities", method = RequestMethod.GET)
    @ResponseBody
    Entities getEntitiesOfClass(@RequestParam String classUrl) {
        return kgServiceLogic.getEntitiesOfClass(classUrl);
    }
}
