package lorabit.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-2-14
 */
@Controller
public class HelloController {
    @RequestMapping(value = "/")
    @ResponseBody
    public Map index() {
        Map map = new HashMap();
        map.put("key", "value");
        return map;
    }
}
