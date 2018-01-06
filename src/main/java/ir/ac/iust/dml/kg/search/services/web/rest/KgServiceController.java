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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

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

    @RequestMapping(value = "/content/getentityclass", method = RequestMethod.GET)
    @ResponseBody
    EntityClasses getEntityClasses(@RequestParam String url) {
        return kgServiceLogic.getEntityClasses(url);
    }

    @RequestMapping(value = "/content/getentities", method = RequestMethod.GET)
    @ResponseBody
    Entities getEntitiesOfClass(@RequestParam String classUrl,
                                @RequestParam int page,
                                @RequestParam int pageSize) {
        return kgServiceLogic.getEntitiesOfClass(classUrl, page, pageSize);
    }


    @RequestMapping(value = "/getentity", method = RequestMethod.GET)
    @ResponseBody
    public APIAnswerList getentity(HttpServletRequest request, @RequestParam(required = false) String query, @RequestParam(required = false) int resultCount) throws Exception {
        System.out.println((new Date()) + "\t request:getentity\t IP:" + request.getRemoteHost() + "\t Query:" + query);
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
    public APIAnswerList getProps(HttpServletRequest request, @RequestParam(required = false) String query, @RequestParam(required = false) int resultCount) throws Exception {
        System.out.println((new Date()) + "\t request:getprops\t IP:" + request.getRemoteHost() + "\t Query:" + query);
        SearchResult uiResults = searcher.search(query);
        APIAnswerList list = new APIAnswerList();
        Collection<List<ResultEntity>> resultGroups = uiResults.getEntities().stream()
                .filter(r -> r.getResultType() == ResultEntity.ResultType.RelationalResult)
                .collect(groupingBy(rE -> rE.getDescription(),
                        Collectors.mapping(Function.identity(),
                                Collectors.toList())))
                .values();
        try {
            int order = 1;
            for (List<ResultEntity> resultGroup:resultGroups) {
                double avgConfidence = 0;

                List<APIPropertySingle> innerResultList = new ArrayList<>();

                for(ResultEntity uiResult:resultGroup) {
                    //Entity OR value?
                    String resultValueType = (uiResult.getLink() != null && uiResult.getLink().toLowerCase().contains("resource")) ? "Entity" : "Value";
                    avgConfidence += uiResult.getConfidence();
                    innerResultList.add(new APIPropertySingle(uiResult.getTitle(), resultValueType, uiResult.getReferenceUri(), uiResult.getLink()));
                }
                avgConfidence = ( avgConfidence != 0 && resultGroup.size() > 0)? avgConfidence / resultGroup.size(): 0;
                APIPropertyGroup propertyGroup = new APIPropertyGroup(order++, innerResultList, avgConfidence);
                list.addAnswer(propertyGroup);
                if (list.getAnswer().size() >= resultCount)
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
