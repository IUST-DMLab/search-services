package ir.ac.iust.dml.kg.search.services.web.rest;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/v1/search/")
@Api(tags = "search", description = "سرویس‌های جستجو")
public class EntitySearchRestServices {

  @RequestMapping(value = "/entity", method = RequestMethod.GET)
  @ResponseBody
  public String root() throws Exception {
    return "Test";
  }
}
