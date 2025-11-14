package org.hibernate;

import org.hibernate.transform.ResultTransformer;

/**
 * Hibernate 5 遺留 API 的極簡替身，只保留 Spring ORM 仍會引用的 ResultTransformer 常數。
 */
public interface Criteria {

    ResultTransformer ROOT_ENTITY = null;

    ResultTransformer DISTINCT_ROOT_ENTITY = null;

    ResultTransformer ALIAS_TO_ENTITY_MAP = null;

}
