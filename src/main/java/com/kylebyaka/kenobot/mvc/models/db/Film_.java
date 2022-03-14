package com.kylebyaka.kenobot.mvc.models.db;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.List;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Film.class)
public class Film_ {

    public static volatile SingularAttribute<Film, String> name;
    public static volatile SingularAttribute<Film, Long> filmId;
    public static volatile ListAttribute<Film, List<Viewer>> votes;
    public static volatile SingularAttribute<Film, Viewer> addedBy;
    public static volatile SingularAttribute<Film, Boolean> viewed;
    public static final String FILM_ID = "filmId";
    public static final String NAME = "name";
    public static final String VIEWED = "viewed";
    public static final String VOTES = "votes";
    public static final String ADDED_BY = "addedBy";
}
