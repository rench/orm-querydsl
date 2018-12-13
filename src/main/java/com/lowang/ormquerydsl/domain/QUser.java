package com.lowang.ormquerydsl.domain;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.StringPath;

/** QUser is a Querydsl query type for User */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

  private static final long serialVersionUID = 1783375467L;

  public static final QUser user = new QUser("user");

  public final StringPath address = createString("address");

  public final StringPath id = createString("id");

  public final BooleanPath locked = createBoolean("locked");

  public final StringPath mobile = createString("mobile");

  public final StringPath name = createString("name");

  public QUser(String variable) {
    super(User.class, forVariable(variable));
  }

  public QUser(Path<? extends User> path) {
    super(path.getType(), path.getMetadata());
  }

  public QUser(PathMetadata metadata) {
    super(User.class, metadata);
  }
}
