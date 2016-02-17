package com.lorabit.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lorabit
 * @since 16-1-27
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Docu implements Serializable {
    private static final long serialVersionUID = 952062962408921136L;
    @Field
    private Integer id;
    @Field
    private String[] username;
    @Field
    private String[] title;
    @Field
    private String[] content;
    @Field
    private Long[] time;
}
