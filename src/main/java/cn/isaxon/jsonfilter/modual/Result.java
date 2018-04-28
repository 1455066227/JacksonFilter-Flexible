package cn.isaxon.jsonfilter.modual;

import java.io.Serializable;

/**
 * <p>http response body</p>
 * <p>Copyright:@isaxon.cn</p>
 *
 * @author saxon/isaxon
 * Create 2018-04-27 10:25 By isaxon
 */
public class Result implements Serializable
{
    public int code;

    public String msg;

    public Object data = null;
}
