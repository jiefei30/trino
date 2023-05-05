/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.sql.tree;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author jiefei
 * @version : AnyValue.java, v 0.1 2023-04-28 15:07 jiefei
 */
public class AnyValue
        extends Expression
{
    private final Expression expression;

    private final Boolean isIgnoreNulls;

    public AnyValue(Expression expression, Boolean isIgnoreNulls)
    {
        this(Optional.empty(), expression, isIgnoreNulls);
    }

    public AnyValue(NodeLocation location, Expression expression, Boolean isIgnoreNulls)
    {
        this(Optional.of(location), expression, isIgnoreNulls);
    }

    protected AnyValue(Optional<NodeLocation> location, Expression expression, Boolean isIgnoreNulls)
    {
        super(location);
        this.expression = expression;
        this.isIgnoreNulls = isIgnoreNulls;
    }

    public Expression getExpression()
    {
        return expression;
    }

    public Boolean getIsIgnoreNulls()
    {
        return isIgnoreNulls;
    }

    @Override
    protected <R, C> R accept(AstVisitor<R, C> visitor, C context)
    {
        return visitor.visitAnyValue(this, context);
    }

    @Override
    public List<? extends Node> getChildren()
    {
        ImmutableList.Builder<Node> nodes = ImmutableList.builder();
        nodes.add(expression);
        return nodes.build();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnyValue anyValue = (AnyValue) o;
        return Objects.equals(expression, anyValue.expression) && Objects.equals(isIgnoreNulls, anyValue.isIgnoreNulls);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(expression, isIgnoreNulls);
    }
}
