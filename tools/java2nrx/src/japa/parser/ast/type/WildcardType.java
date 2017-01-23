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
 * Created on 05/10/2006
 */
package japa.parser.ast.type;

import japa.parser.ast.visitor.GenericVisitor;
import japa.parser.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class WildcardType extends Type {

    private ReferenceType ext;

    private ReferenceType sup;

    public WildcardType() {
    }

    public WildcardType(ReferenceType ext) {
        this.ext = ext;
    }

    public WildcardType(ReferenceType ext, ReferenceType sup) {
        this.ext = ext;
        this.sup = sup;
    }

    public WildcardType(int beginLine, int beginColumn, int endLine, int endColumn, ReferenceType ext, ReferenceType sup) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.ext = ext;
        this.sup = sup;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public ReferenceType getExtends() {
        return ext;
    }

    public ReferenceType getSuper() {
        return sup;
    }

    public void setExtends(ReferenceType ext) {
        this.ext = ext;
    }

    public void setSuper(ReferenceType sup) {
        this.sup = sup;
    }

}
