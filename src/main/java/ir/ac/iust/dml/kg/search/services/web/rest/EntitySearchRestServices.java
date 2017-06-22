package ir.ac.iust.dml.kg.search.services.web.rest;

import io.swagger.annotations.Api;
import ir.ac.iust.dml.kg.search.logic.Searcher;
import ir.ac.iust.dml.kg.search.logic.data.SearchResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/v1/searcher/")
@Api(tags = "searcher", description = "سرویس‌های جستجو")
public class EntitySearchRestServices {

    final private Searcher searcher = Searcher.getInstance();

  public EntitySearchRestServices() throws Exception {
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @ResponseBody
  public SearchResult search(@RequestParam(required = false) String keyword) throws Exception {
    return searcher.search(keyword);
  }

}
