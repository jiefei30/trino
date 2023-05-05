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
package io.trino.sql.parser;
import io.trino.sql.sparkSQL.SparkSQLParser;
import io.trino.sql.sparkSQL.SparkSQLParserBaseVisitor;
import io.trino.sql.tree.AnyValue;
import io.trino.sql.tree.Expression;
import io.trino.sql.tree.Node;
import io.trino.sql.tree.NodeLocation;
import io.trino.sql.tree.StringLiteral;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * @author jiefei
 * @version : SparkSQLAstBuilder.java, v 0.1 2023-04-28 15:56 jiefei
 */
public class SparkSQLAstBuilder
        extends SparkSQLParserBaseVisitor<Node>
{
    private int parameterPosition;
    private final ParsingOptions parsingOptions;

    SparkSQLAstBuilder(ParsingOptions parsingOptions)
    {
        this.parsingOptions = requireNonNull(parsingOptions, "parsingOptions is null");
    }

    @Override
    public Node visitSingleStatement(SparkSQLParser.SingleStatementContext context)
    {
        return visit(context.statement());
    }

    @Override
    public Node visitSingleExpression(SparkSQLParser.SingleExpressionContext context)
    {
        return visit(context.namedExpression());
    }

    @Override
    public Node visitAny_value(SparkSQLParser.Any_valueContext context)
    {
        return new AnyValue(
                getLocation(context),
                (Expression) visit(context.expression()),
                Objects.nonNull(context.IGNORE()));
    }

    @Override
    public Node visitStringLiteral(SparkSQLParser.StringLiteralContext context)
    {
        return new StringLiteral(getLocation(context), unquote(context.getText()));
    }

    private static String unquote(String value)
    {
        return value.substring(1, value.length() - 1)
                .replace("''", "'");
    }

    public static NodeLocation getLocation(TerminalNode terminalNode)
    {
        requireNonNull(terminalNode, "terminalNode is null");
        return getLocation(terminalNode.getSymbol());
    }

    public static NodeLocation getLocation(ParserRuleContext parserRuleContext)
    {
        requireNonNull(parserRuleContext, "parserRuleContext is null");
        return getLocation(parserRuleContext.getStart());
    }

    public static NodeLocation getLocation(Token token)
    {
        requireNonNull(token, "token is null");
        return new NodeLocation(token.getLine(), token.getCharPositionInLine() + 1);
    }
}
