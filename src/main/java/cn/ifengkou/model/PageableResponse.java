package cn.ifengkou.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/23
 */
@Data
public class PageableResponse<T> implements Serializable {
    private List<T> data=new ArrayList<>();
    private int draw;
    private long filteredNum;
    private long totalNum;
}
