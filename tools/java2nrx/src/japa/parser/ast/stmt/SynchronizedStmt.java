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
 * Created on 18/11/2006
 */
package japa.parser.ast.stmt;

import japa.parser.ast.expr.Expression;
import japa.parser.ast.visitor.GenericVisitor;
import japa.parser.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class SynchronizedStmt extends Statement {

    private Expression expr;

    private BlockStmt block;

    public SynchronizedStmt() {
    }

    public SynchronizedStmt(Expression expr, BlockStmt block) {
        this.expr = expr;
        this.block = block;
    }

    public SynchronizedStmt(int beginLine, int beginColumn, int endLine, int endColumn, Expression expr, BlockStmt block) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.expr = expr;
        this.block = block;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public BlockStmt getBlock() {
        return block;
    }

    public Expression getExpr() {
        return expr;
    }

    public void setBlock(BlockStmt block) {
        this.block = block;
    }

    public void setExpr(Expression expr) {
        this.expr = expr;
    }

}
