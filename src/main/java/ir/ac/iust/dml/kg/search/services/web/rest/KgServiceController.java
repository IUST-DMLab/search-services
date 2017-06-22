package ir.ac.iust.dml.kg.search.services.web.rest;

import io.swagger.annotations.Api;
import ir.ac.iust.dml.kg.search.logic.Searcher;
import ir.ac.iust.dml.kg.search.logic.data.ResultEntity;
import ir.ac.iust.dml.kg.search.logic.data.SearchResult;
import ir.ac.iust.dml.kg.search.logic.kgservice.KgServiceLogic;
import ir.ac.iust.dml.kg.search.logic.kgservice.data.*;
import ir.ac.iust.dml.kg.search.services.Types.APIAnswerList;
import ir.ac.iust.dml.kg.search.services.Types.APIEntity;
import ir.ac.iust.dml.kg.search.services.Types.APIPropertyGroup;
import ir.ac.iust.dml.kg.search.services.Types.APIPropertySingle;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/kgservice")
@Api(tags = "kgservice", description = "سرویس‌های درخواست شده از مرکز تحقیقات")
public class KgServiceController {

    final private Searcher searcher = Searcher.getInstance();
    final private KgServiceLogic kgServiceLogic = new KgServiceLogic();

    public KgServiceController() throws Exception {
    }

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


    @RequestMapping(value = "/getentity", method = RequestMethod.GET)
    @ResponseBody
    public APIAnswerList getentity(@RequestParam(required = false) String query, @RequestParam(required = false) int resultCount) throws Exception {
        SearchResult uiResults = searcher.search(query);
        APIAnswerList list = new APIAnswerList();
        int order = 1;
        try {
            for (ResultEntity uiResult : uiResults.getEntities()) {
                if (uiResult.getResultType() == ResultEntity.ResultType.Entity) {
                    APIEntity apiEntity = new APIEntity(order++, uiResult.getTitle(), uiResult.getLink(), uiResult.getConfidence());
                    list.addAnswer(apiEntity);
                }
                if (list.getAnswer().size() >= resultCount)
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    @RequestMapping(value = "/getprops", method = RequestMethod.GET)
    @ResponseBody
    public APIAnswerList getProps(@RequestParam(required = false) String query, @RequestParam(required = false) int resultCount) throws Exception {
        SearchResult uiResults = searcher.search(query);
        APIAnswerList list = new APIAnswerList();

        try {
            int order = 1;
            for (ResultEntity uiResult : uiResults.getEntities()) {
                if (uiResult.getResultType() == ResultEntity.ResultType.RelationalResult) {
                    //Entity OR value?
                    String resultValueType = (uiResult.getLink() != null && uiResult.getLink().toLowerCase().contains("resource")) ? "Entity" : "Value";

                    //Inner list --> Each item in a separate list (temporarily)
                    List<APIPropertySingle> innerResultList = new ArrayList<>();
                    innerResultList.add(new APIPropertySingle(uiResult.getTitle(), resultValueType, uiResult.getReferenceUri(), uiResult.getLink()));

                    APIPropertyGroup propertyGroup = new APIPropertyGroup(order++, innerResultList, uiResult.getConfidence());
                    list.addAnswer(propertyGroup);
                }
                if (list.getAnswer().size() >= resultCount)
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
