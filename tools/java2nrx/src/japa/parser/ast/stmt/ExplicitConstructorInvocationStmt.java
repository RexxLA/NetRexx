/*
 * Copyright (C) 2007 Julio Vilmar Gesser.
 * 
 * This file is part of Java 1.5 parser and Abstract Syntax Tree.
 *
 * Java 1.5 parser and Abstract Syntax Tree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java 1.5 parser and Abstract Syntax Tree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java 1.5 parser and Abstract Syntax Tree.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 03/11/2006
 */
package japa.parser.ast.stmt;

import japa.parser.ast.expr.Expression;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.GenericVisitor;
import japa.parser.ast.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class ExplicitConstructorInvocationStmt extends Statement {

    private List<Type> typeArgs;

    private boolean isThis;

    private Expression expr;

    private List<Expression> args;

    public ExplicitConstructorInvocationStmt() {
    }

    public ExplicitConstructorInvocationStmt(boolean isThis, Expression expr, List<Expression> args) {
        this.isThis = isThis;
        this.expr = expr;
        this.args = args;
    }

    public ExplicitConstructorInvocationStmt(int beginLine, int beginColumn, int endLine, int endColumn, List<Type> typeArgs, boolean isThis, Expression expr, List<Expression> args) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.typeArgs = typeArgs;
        this.isThis = isThis;
        this.expr = expr;
        this.args = args;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public List<Expression> getArgs() {
        return args;
    }

    public Expression getExpr() {
        return expr;
    }

    public List<Type> getTypeArgs() {
        return typeArgs;
    }

    public boolean isThis() {
        return isThis;
    }

    public void setArgs(List<Expression> args) {
        this.args = args;
    }

    public void setExpr(Expression expr) {
        this.expr = expr;
    }

    public void setThis(boolean isThis) {
        this.isThis = isThis;
    }

    public void setTypeArgs(List<Type> typeArgs) {
        this.typeArgs = typeArgs;
    }
}
