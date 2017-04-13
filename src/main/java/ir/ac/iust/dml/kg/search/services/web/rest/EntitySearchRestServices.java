package ir.ac.iust.dml.kg.search.services.web.rest;

import io.swagger.annotations.Api;
import ir.ac.iust.dml.kg.search.logic.FakeLogic;
import ir.ac.iust.dml.kg.search.logic.data.SearchResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/v1/searcher/")
@Api(tags = "searcher", description = "سرویس‌های جستجو")
public class EntitySearchRestServices {

  @RequestMapping(value = "/fake1", method = RequestMethod.GET)
  @ResponseBody
  public SearchResult fake1(@RequestParam(required = false) String keyword) throws Exception {
    return FakeLogic.oneEntity();
  }

  @RequestMapping(value = "/fake2", method = RequestMethod.GET)
  @ResponseBody
  public SearchResult fake2(@RequestParam(required = false) String keyword) throws Exception {
    return FakeLogic.oneEntityAndBreadcrumb();
  }

  @RequestMapping(value = "/fake3", method = RequestMethod.GET)
  @ResponseBody
  public SearchResult fake3(@RequestParam(required = false) String keyword) throws Exception {
    return FakeLogic.list();
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @ResponseBody
  public SearchResult search(@RequestParam(required = false) String keyword) throws Exception {
    return FakeLogic.search(keyword);
  }
}
