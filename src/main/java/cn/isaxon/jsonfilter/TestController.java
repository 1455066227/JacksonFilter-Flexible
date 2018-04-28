package cn.isaxon.jsonfilter;

import cn.isaxon.jsonfilter.annotation.JsonFilterDesensitized;
import cn.isaxon.jsonfilter.modual.Comment;
import cn.isaxon.jsonfilter.modual.Result;
import cn.isaxon.jsonfilter.modual.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p></p>
 * <p>Copyright:@isaxon.cn</p>
 *
 * @author saxon/isaxon
 * Create 2018-04-27 9:39 PM By isaxon
 */
@RestController
public class TestController
{
    private static Result result = new Result();
    static
    {
        User user = new User();
        user.age = 18;
        user.id = 1;
        user.tel = "181xxxx6666";
        user.name = "isaxon";

        Comment comment = new Comment();
        comment.id = 1;
        comment.gmt_delete = 0L;
        comment.is_delete = 0;
        comment.user = user;

        result.code = 1;
        result.msg = "SUCCESS";
        result.data = comment;
    }

    @GetMapping("/test")
    public Result test()
    {
        return result;
    }

    @JsonFilterDesensitized({
            @JsonFilterDesensitized.ClassFieldFilter(type = User.class, include = {"id", "name", "age"}),
            @JsonFilterDesensitized.ClassFieldFilter(type = Comment.class, exclude = {"is_delete", "gmt_delete"})
    })
    @GetMapping("/testJsonFilter")
    public Result testJsonFilter()
    {

        return result;
    }
}
