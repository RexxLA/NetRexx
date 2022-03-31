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
 * Created on 04/11/2006
 */
package japa.parser.ast.stmt;

import japa.parser.ast.visitor.GenericVisitor;
import japa.parser.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class LabeledStmt extends Statement {

    private String label;

    private Statement stmt;

    public LabeledStmt() {
    }

    public LabeledStmt(String label, Statement stmt) {
        this.label = label;
        this.stmt = stmt;
    }

    public LabeledStmt(int beginLine, int beginColumn, int endLine, int endColumn, String label, Statement stmt) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.label = label;
        this.stmt = stmt;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public String getLabel() {
        return label;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }
}
