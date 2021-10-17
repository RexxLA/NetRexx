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
/*
 * 
 * Modified on 28/09/2011
 * 
 * Copyright (C) 2011 Marc Remes
 * remesm@gmail.com   
 * 
 * Modified to translate the Java AST to NetRexx
 * 
 */
package japa.parser.ast.visitor;

import japa.parser.ast.BlockComment;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.LineComment;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.AnnotationDeclaration;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EmptyMemberDeclaration;
import japa.parser.ast.body.EmptyTypeDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ArrayAccessExpr;
import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.CharLiteralExpr;
import japa.parser.ast.expr.ClassExpr;
import japa.parser.ast.expr.ConditionalExpr;
import japa.parser.ast.expr.DoubleLiteralExpr;
import japa.parser.ast.expr.EnclosedExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.InstanceOfExpr;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.IntegerLiteralMinValueExpr;
import japa.parser.ast.expr.LongLiteralExpr;
import japa.parser.ast.expr.LongLiteralMinValueExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.expr.UnaryExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.AssertStmt;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.BreakStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.ContinueStmt;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.EmptyStmt;
import japa.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.LabeledStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.SwitchEntryStmt;
import japa.parser.ast.stmt.SwitchStmt;
import japa.parser.ast.stmt.SynchronizedStmt;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.stmt.TypeDeclarationStmt;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;
import japa.parser.ast.type.WildcardType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */

public final class DumpVisitor implements VoidVisitor<Object> {
	
	private static int classDepth = 0;
	private static int anonymousCount = 0;
	private String parentClass = "";
	private static int inAnonymousClass = 0;
	private String anonymousMethod = "";  

	private static class SourcePrinter {

		private int level = 0;

		private boolean indented = false;
		private int index;
		

		//@SuppressWarnings("rawtypes")
		private final ArrayList StringBuilderArrayList = new ArrayList();
		
		
		private StringBuilder buf;

		
		public void indent() {
			level++;
		}

		public void unindent() {
			level--;
		}

		private void makeIndent() {
			index = classDepth;
			if (index > 0) {
				index = index - 1;
			}
			
			if (StringBuilderArrayList.size() < (index + 1)) {
				StringBuilder newbuf = new StringBuilder();
				StringBuilderArrayList.add(newbuf);
				buf = newbuf;
			} else {
				buf = (StringBuilder) StringBuilderArrayList.get(index);
			}
			
			for (int i = 0; i < level; i++) {
				buf.append("    ");
			}	
		}

		public void print(String arg) {
			if (!indented) {
				makeIndent();
				indented = true;
			}
			index = classDepth;
			if (index > 0) {
				index = index - 1;
			}
			
			if (StringBuilderArrayList.size() < (index + 1)) {
				StringBuilder newbuf = new StringBuilder();
				StringBuilderArrayList.add(newbuf);
				buf = newbuf;
			} else {
				buf = (StringBuilder) StringBuilderArrayList.get(index);
			}
			
			buf.append(arg);

			// System.out.println(buf + "\n");
			// System.out.flush();
		}

		public void printLn(String arg) {
			print(arg);
			printLn();
		}

		public void printLn() {
			index = classDepth;
			if (index > 0) {
				index = index - 1;
			}

			if (StringBuilderArrayList.size() < (index + 1)) {
				StringBuilder newbuf = new StringBuilder();
				StringBuilderArrayList.add(newbuf);
				buf = newbuf;
			} else {
				buf = (StringBuilder) StringBuilderArrayList.get(index);
			}

			buf.append("\n");
			indented = false;

			// System.out.println(buf);
			// System.out.flush();
		}

		public String getSource() {
			int i;
			StringBuilder buffer = new StringBuilder();
			StringBuilder buf;
			for (i = 0; i < StringBuilderArrayList.size(); i++) {
				buf = (StringBuilder) StringBuilderArrayList.get(i);
				buffer.append(buf.toString());
 			}
			return buffer.toString();
		}
		
		@Override
		public String toString() {
			return getSource();
		}
	}
  
	private final SourcePrinter printer = new SourcePrinter();

	public String getSource() {
		return printer.getSource();
	}

	private void printModifiers(int modifiers) {
		if (ModifierSet.isPrivate(modifiers)) {
			printer.print("private ");
		}
		if (ModifierSet.isProtected(modifiers)) {
			printer.print("inheritable ");
		}
		if (ModifierSet.isPublic(modifiers)) {
			printer.print("public ");
		}
		if (ModifierSet.isAbstract(modifiers)) {
			printer.print("abstract ");
		}
		if (ModifierSet.isStatic(modifiers)) {
			if (!ModifierSet.isFinal(modifiers)) {
				printer.print("static ");
			}
//			printer.print("static ");
		}
		if (ModifierSet.isFinal(modifiers)) {
			// printer.print("final ");
			printer.print("constant ");

		}
		if (ModifierSet.isNative(modifiers)) {
			printer.print("native ");
		}
		if (ModifierSet.isStrictfp(modifiers)) {
			printer.print("strictfp ");
		}
		if (ModifierSet.isSynchronized(modifiers)) {
			printer.print("protect ");
		}
		if (ModifierSet.isTransient(modifiers)) {
			printer.print("transient ");
		}
		if (ModifierSet.isVolatile(modifiers)) {
			printer.print("volatile ");
		}
	}

	private void printMethodVisibility(int modifiers) {
		if (ModifierSet.isPrivate(modifiers)) {
			printer.print("private ");
		}
		// is default
		// if (ModifierSet.isPublic(modifiers)) {
		// 		printer.print("public ");
		// }
		if (ModifierSet.isAbstract(modifiers)) {
			printer.print("abstract ");
		}
		if (ModifierSet.isStrictfp(modifiers)) {
			printer.print("/* **nonrx**  strictfp ** */");
		}
		if (ModifierSet.isSynchronized(modifiers)) {
			printer.print("protect ");
		}
		if (ModifierSet.isTransient(modifiers)) {
			printer.print("transient ");
		}
		if (ModifierSet.isVolatile(modifiers)) {
			printer.print("/* **nonrx**  volatile ** */");
		}
	}

	private void printMethodModifiers(int modifiers) {
		// if (ModifierSet.isPrivate(modifiers)) {
		// printer.print("private ");
		// }
		if (ModifierSet.isProtected(modifiers)) {
			printer.print("shared ");
		}
		if (ModifierSet.isAbstract(modifiers)) {
			printer.print("abstract ");
		}
		if (ModifierSet.isStatic(modifiers)) {
			printer.print("static ");
		}
		if (ModifierSet.isFinal(modifiers)) {
			printer.print("final ");
		}
		if (ModifierSet.isNative(modifiers)) {
			printer.print("native ");
		}

	}

	private void printClassVisibility(int modifiers) {
		if (ModifierSet.isPrivate(modifiers)) {
			printer.print("private ");
		}
		if (ModifierSet.isPublic(modifiers)) {
			printer.print("public ");
		}
	}

	private void printClassModifiers(int modifiers,
			ClassOrInterfaceDeclaration n) {

		if (ModifierSet.isAbstract(modifiers)) {
			printer.print("abstract ");
		}
		if (ModifierSet.isFinal(modifiers)) {
			printer.print("final ");
		}
		if (n.isInterface()) {
			printer.print("interface ");
		}

	}

	private void printMembers(List<BodyDeclaration> members, Object arg) {
		for (BodyDeclaration member : members) {
			printer.printLn();
			member.accept(this, arg);
			printer.printLn();
		}
	}

	private void printMemberAnnotations(List<AnnotationExpr> annotations,
			Object arg) {
		if (annotations != null) {
			for (AnnotationExpr a : annotations) {
				printer.print("/* **nonrx**  ");
				a.accept(this, arg);
				printer.print(" ** */");
				printer.printLn();
			}
		}
	}

	private void printAnnotations(List<AnnotationExpr> annotations, Object arg) {
		if (annotations != null) {
			for (AnnotationExpr a : annotations) {
				printer.print("/* **nonrx**  ");
				a.accept(this, arg);
				printer.print(" ** */");

				printer.printLn();
			}
		}
	}

	private void printTypeArgs(List<Type> args, Object arg) {
		if (args != null) {
			printer.print(" /* **nonrx**  <");

			for (Iterator<Type> i = args.iterator(); i.hasNext();) {
				Type t = i.next();
				t.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}

			printer.print("> ** */");
		}
	}

	private void printTypeParameters(List<TypeParameter> args, Object arg) {
		if (args != null) {
			printer.print("/* **nonrx**  <");
 
			for (Iterator<TypeParameter> i = args.iterator(); i.hasNext();) {
				TypeParameter t = i.next();
				t.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}		
			printer.print("> ** */");
		}
	}

	private void printArguments(List<Expression> args, Object arg) {
		printer.print("(");
		if (args != null) {
			for (Iterator<Expression> i = args.iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.print(")");
	}

	private void printJavadoc(JavadocComment javadoc, Object arg) {
		if (javadoc != null) {
			javadoc.accept(this, arg);
		}
	}

	public void visit(CompilationUnit n, Object arg) {
		if (n.getPackage() != null) {
			n.getPackage().accept(this, arg);
		}
		if (n.getImports() != null) {
			for (ImportDeclaration i : n.getImports()) {
				i.accept(this, arg);
			}
			printer.printLn();
		}
		if (n.getTypes() != null) {
			for (Iterator<TypeDeclaration> i = n.getTypes().iterator(); i
					.hasNext();) {
				i.next().accept(this, arg);
				printer.printLn();
				if (i.hasNext()) {
					printer.printLn();
				}
			}
		}
	}

	public void visit(PackageDeclaration n, Object arg) {
		printAnnotations(n.getAnnotations(), arg);
		printer.print("package ");
		n.getName().accept(this, arg);
		printer.printLn();
		printer.printLn();
	}

	public void visit(NameExpr n, Object arg) {
		printer.print(n.getName());
	}

	public void visit(QualifiedNameExpr n, Object arg) {
		n.getQualifier().accept(this, arg);
		printer.print(".");
		printer.print(n.getName());
	}

	public void visit(ImportDeclaration n, Object arg) {

		printer.print("import ");
		// if (n.isStatic()) {
		// printer.print("static ");
		// }
		n.getName().accept(this, arg);
		if (n.isAsterisk()) {
			printer.print(".");
		}
		printer.printLn("");
	}

	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
		// int modifiers = n.getModifiers();
        
		classDepth++;
        
		printJavadoc(n.getJavaDoc(), arg);
		printer.print("class ");

		printer.print(parentClass + n.getName());
		printer.print(" ");
		if (classDepth > 1) {
			printer.print("dependent ");
		}
		
		parentClass = parentClass + n.getName() + "."; 

		printClassVisibility(n.getModifiers());
		printClassModifiers(n.getModifiers(), n);

		if (n.getExtends() != null) {
			printer.print(" extends ");
			for (Iterator<ClassOrInterfaceType> i = n.getExtends().iterator(); i
					.hasNext();) {
				ClassOrInterfaceType c = i.next();
				c.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}

		if (n.getImplements() != null) {
			printer.print(" implements ");
			for (Iterator<ClassOrInterfaceType> i = n.getImplements()
					.iterator(); i.hasNext();) {
				ClassOrInterfaceType c = i.next();
				c.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.printLn(" ");

		// printer.printLn(" {");
		printer.indent();
		if (n.getMembers() != null) {
			printMembers(n.getMembers(), arg);
		}
		printer.unindent();
		
		classDepth--;
		parentClass = parentClass.substring(0, parentClass.lastIndexOf(n.getName()+ "."));
		
		// printer.print("}");
	}

	public void visit(EmptyTypeDeclaration n, Object arg) {
		printJavadoc(n.getJavaDoc(), arg);
		// printer.print(";");
	}

	public void visit(JavadocComment n, Object arg) {
		printer.print("/**");
		printer.print(n.getContent());
		printer.printLn("*/");
	}

	public void visit(ClassOrInterfaceType n, Object arg) {
		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
			printer.print(".");
		}
		printer.print(n.getName());
		printTypeArgs(n.getTypeArgs(), arg);
	}

	public void visit(TypeParameter n, Object arg) {
		printer.print(n.getName());
		if (n.getTypeBound() != null) {
			printer.print(" extends ");
			for (Iterator<ClassOrInterfaceType> i = n.getTypeBound().iterator(); i
					.hasNext();) {
				ClassOrInterfaceType c = i.next();
				c.accept(this, arg);
				if (i.hasNext()) {
					printer.print(" & ");
				}
			}
		}
	}

	public void visit(PrimitiveType n, Object arg) {
		switch (n.getType()) {
		case Boolean:
			printer.print("boolean");
			break;
		case Byte:
			printer.print("byte");
			break;
		case Char:
			printer.print("char");
			break;
		case Double:
			printer.print("double");
			break;
		case Float:
			printer.print("float");
			break;
		case Int:
			printer.print("int");
			break;
		case Long:
			printer.print("long");
			break;
		case Short:
			printer.print("short");
			break;
		}
	}

	public void visit(ReferenceType n, Object arg) {
		n.getType().accept(this, arg);
		for (int i = 0; i < n.getArrayCount(); i++) {
			printer.print("[]");
		}
	}

	public void visit(WildcardType n, Object arg) {
		printer.print("?");
		if (n.getExtends() != null) {
			printer.print(" extends ");
			n.getExtends().accept(this, arg);
		}
		if (n.getSuper() != null) {
			printer.print(" super ");
			n.getSuper().accept(this, arg);
		}
	}

	public void visit(FieldDeclaration n, Object arg) {
		printJavadoc(n.getJavaDoc(), arg);
		printMemberAnnotations(n.getAnnotations(), arg);

		printer.print("properties ");

		printModifiers(n.getModifiers());
		printer.printLn();

		// n.getType().accept(this, arg);

		// printer.print(" ");
		for (Iterator<VariableDeclarator> i = n.getVariables().iterator(); i
				.hasNext();) {
			VariableDeclarator var = i.next();
			// var.accept(this, arg);
			printer.print(var.getId().toString());
			printer.print(" = ");
			printer.print(n.getType().toString());
			printer.print(" ");
			if (var.getInit() != null) {
				printer.print(var.getInit().toString());
			}

			if (i.hasNext()) {
				printer.printLn();
			}
		}

		// printer.print(";");
	}

	public void visit(VariableDeclarator n, Object arg) {
		n.getId().accept(this, arg);
		if (n.getInit() != null) {
			printer.print(" = ");
			n.getInit().accept(this, arg);
		}
	}

	public void visit(VariableDeclaratorId n, Object arg) {
		printer.print(n.getName());
		for (int i = 0; i < n.getArrayCount(); i++) {
			printer.print("[]");
		}
	}

	public void visit(ArrayInitializerExpr n, Object arg) {
//		printer.print("[");
		if (n.getValues() != null) {
			printer.print("[");

			printer.print(" ");
			for (Iterator<Expression> i = n.getValues().iterator(); i.hasNext();) {
				Expression expr = i.next();
				expr.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
			printer.print(" ");
			printer.print("]");

		}
//		printer.print("]");
	}

	public void visit(VoidType n, Object arg) {
		printer.print("void");
	}

	public void visit(ArrayAccessExpr n, Object arg) {
		n.getName().accept(this, arg);
		printer.print("[");
		n.getIndex().accept(this, arg);
		printer.print("]");
	}

	public void visit(ArrayCreationExpr n, Object arg) {
		// printer.print("new ");
		n.getType().accept(this, arg);

		if (n.getDimensions() != null) {
			for (Expression dim : n.getDimensions()) {
				printer.print("[");
				dim.accept(this, arg);
				printer.print("]");
			}
			for (int i = 0; i < n.getArrayCount(); i++) {
				printer.print("[]");
			}
		} else {
			for (int i = 0; i < n.getArrayCount(); i++) {
				printer.print("[]");
			}
			printer.print(" ");
			n.getInitializer().accept(this, arg);
		}
	}

	public void visit(AssignExpr n, Object arg) {
		n.getTarget().accept(this, arg);
		printer.print(" ");
		switch (n.getOperator()) {
		case assign:
			printer.print("=");
			break;
		case and:
			printer.print("/* **nonrx** &= ** */");
			break;
		case or:
			printer.print("/* **nonrx** |= ** */");
			break;
		case xor:
			printer.print("/* **nonrx** ^= ** */");
			break;
		case plus:
			printer.print("= ");
			printer.print(n.getTarget().toString());
			printer.print("+");
			break;
		case minus:
			printer.print("= ");
			printer.print(n.getTarget().toString());
			printer.print("-");
			break;
		case rem:
			printer.print("=");
			printer.print(n.getTarget().toString());
			printer.print("//");
			break;
		case slash:
			printer.print("= ");
			printer.print(n.getTarget().toString());
			printer.print("/");

			break;
		case star:
			printer.print("= ");
			printer.print(n.getTarget().toString());
			printer.print("*");
			break;
		case lShift:
			printer.print("/* **nonrx** <<= ** */");
			break;
		case rSignedShift:
			printer.print("/* **nonrx** >>= ** */");
			break;
		case rUnsignedShift:
			printer.print("/* **nonrx** >>>= ** */");
			break;
		}
		printer.print(" ");
		n.getValue().accept(this, arg);
	}

	public void visit(BinaryExpr n, Object arg) {
		n.getLeft().accept(this, arg);
		printer.print(" ");
		switch (n.getOperator()) {
		case or:
			printer.print("|");
			break;
		case and:
			printer.print("&");
			break;
		case binOr:
			printer.print(" /* **nonrx**  | ** */ ");
			break;
		case binAnd:
			printer.print(" /* **nonrx**  & ** */ ");
			break;
		case xor:
			printer.print("&&");
			break;
		case equals:
			printer.print("==");
			break;
		case notEquals:
			printer.print("\\==");
			break;
		case less:
			printer.print("<");
			break;
		case greater:
			printer.print(">");
			break;
		case lessEquals:
			printer.print("<=");
			break;
		case greaterEquals:
			printer.print(">=");
			break;
		case lShift:
			printer.print(" /* **nonrx**  << ** */");
			break;
		case rSignedShift:
			printer.print(" /* **nonrx**  >> ** */");
			break;
		case rUnsignedShift:
			printer.print(" /* **nonrx**  >>> ** */");
			break;
		case plus:
			if (n.getLeft().getClass().toString().matches("class japa.parser.ast.expr.StringLiteralExpr") ||
				n.getRight().getClass().toString().matches("class japa.parser.ast.expr.StringLiteralExpr")) {
				printer.print("||");
			} else {
				printer.print("+");
			}
			break;
		case minus:
			printer.print("-");
			break;
		case times:
			printer.print("*");
			break;
		case divide:
			printer.print("/");
			break;
		case remainder:
			printer.print("//");
			break;
		}
		printer.print(" ");
		n.getRight().accept(this, arg);
	}

	public void visit(CastExpr n, Object arg) {
		printer.print("(");
		n.getType().accept(this, arg);
		printer.print(") ");
		n.getExpr().accept(this, arg);
	}

	public void visit(ClassExpr n, Object arg) {
		n.getType().accept(this, arg);
		printer.print(".class");
	}

	public void visit(ConditionalExpr n, Object arg) {
		// n.getCondition().accept(this, arg);
		// printer.print(" ? ");
		// n.getThenExpr().accept(this, arg);
		// printer.print(" : ");
		// n.getElseExpr().accept(this, arg);
		printer.print(" if ");
		n.getCondition().accept(this, arg);
		printer.print(" then do ");
		n.getThenExpr().accept(this, arg);
		printer.printLn(" end ");
		printer.printLn(" else do ");
		n.getElseExpr().accept(this, arg);
		printer.printLn(" end ");
	}

	public void visit(EnclosedExpr n, Object arg) {
		printer.print("(");
		n.getInner().accept(this, arg);
		printer.print(")");
	}

	public void visit(FieldAccessExpr n, Object arg) {
		n.getScope().accept(this, arg);
		printer.print(".");
		printer.print(n.getField());
	}

	public void visit(InstanceOfExpr n, Object arg) {
		n.getExpr().accept(this, arg);
//		printer.print(" instanceof ");
		printer.print(" <= ");
		n.getType().accept(this, arg);
	}

	public void visit(CharLiteralExpr n, Object arg) {
		printer.print("'");
		printer.print(n.getValue());
		printer.print("'");
	}

	public void visit(DoubleLiteralExpr n, Object arg) {
		String literal_to_chop = (n.getValue()).toString();
		if ((literal_to_chop.substring(literal_to_chop.length() - 1)).equals("d")) {
			printer.print(literal_to_chop.substring(0, literal_to_chop.length()-1));
		} else if ((literal_to_chop.substring(literal_to_chop.length() - 1)).equals("f")) {
			printer.print(literal_to_chop.substring(0, literal_to_chop.length()-1));
		} else {
			printer.print(n.getValue());
		}
	}

	public void visit(IntegerLiteralExpr n, Object arg) {
		printer.print(n.getValue());
	}

	public void visit(LongLiteralExpr n, Object arg) {
		String literal_to_chop = (n.getValue()).toString();
		if ((literal_to_chop.substring(literal_to_chop.length() - 1)).equals("L")) {
			printer.print(literal_to_chop.substring(0, literal_to_chop.length()-1));
		} else {
			printer.print(n.getValue());
		}
	}

	public void visit(IntegerLiteralMinValueExpr n, Object arg) {
		printer.print(n.getValue());
	}

	public void visit(LongLiteralMinValueExpr n, Object arg) {
		printer.print(n.getValue());
	}

	public void visit(StringLiteralExpr n, Object arg) {
		printer.print("\"");
		printer.print(n.getValue());
		printer.print("\"");
	}

	public void visit(BooleanLiteralExpr n, Object arg) {
		if (n.getValue()) {
			printer.print("1");
		} else {
			printer.print("0");
		}
	}

	public void visit(NullLiteralExpr n, Object arg) {
		printer.print("null");
	}

	public void visit(ThisExpr n, Object arg) {
		if (n.getClassExpr() != null) {
			n.getClassExpr().accept(this, arg);
			printer.print(".");
		}
		printer.print("this");
	}

	public void visit(SuperExpr n, Object arg) {
		if (n.getClassExpr() != null) {
			n.getClassExpr().accept(this, arg);
			printer.print(".");
		}
		printer.print("super");
	}

	public void visit(MethodCallExpr n, Object arg) {
		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
			printer.print(".");
		}
		printTypeArgs(n.getTypeArgs(), arg);
		printer.print(n.getName());
		printArguments(n.getArgs(), arg);
	}

	public void visit(ObjectCreationExpr n, Object arg) {
		if (n.getScope() != null) {
			n.getScope().accept(this, arg);
			printer.print(".");
		}

// 		printer.print("new ");

//		printTypeArgs(n.getTypeArgs(), arg);
//		n.getType().accept(this, arg);

//		printArguments(n.getArgs(), arg);

		if (n.getAnonymousClassBody() != null) {

			printer.print(" /* ** anonymous class ** */ ");
			
			String className = "__anon_" + anonymousCount + "_";
			printer.print(parentClass + className);
			printTypeArgs(n.getTypeArgs(), arg);

			n.getType().accept(this, arg);
			printArguments(n.getArgs(), arg);

			
			classDepth++;
			inAnonymousClass++;
			
			printer.print("class ");
			printer.print(parentClass + className);
			n.getType().accept(this, arg);
			printer.print(" private ");
			printer.print(" extends /** or implements ? **/ ");
			n.getType().accept(this, arg);
			
			parentClass = parentClass + className + ".";
			printer.indent();
			
			printMembers(n.getAnonymousClassBody(), arg);
			printer.printLn();

			printer.unindent();

			anonymousCount++;
			inAnonymousClass--;
			classDepth--;
			
			parentClass = parentClass.substring(0, parentClass.lastIndexOf(className+ "."));
			
			
// ORIG		printer.printLn(" {");
//			printer.indent();
//			printMembers(n.getAnonymousClassBody(), arg);
//			printer.unindent();
//			printer.print("}");
		} else {
			printTypeArgs(n.getTypeArgs(), arg);
			n.getType().accept(this, arg);
			
			printArguments(n.getArgs(), arg);
		}
	}

	public void visit(UnaryExpr n, Object arg) {
		switch (n.getOperator()) {
		case positive:
			printer.print("+");
			printer.print(n.getExpr().toString());
			break;
		case negative:
			printer.print("-");
			printer.print(n.getExpr().toString());
			break;
		case inverse:
			printer.print("~");
			break;
		case not:
			printer.print("\\");
			printer.print(n.getExpr().toString());
			break;
		case preIncrement:
			printer.print("++");
			break;
		case preDecrement:
			printer.print("--");
			break;
		}

		// n.getExpr().accept(this, arg);

		switch (n.getOperator()) {
		case posIncrement:
			// printer.print("++");
			printer.print(n.getExpr().toString());
			printer.print(" = ");
			printer.print(n.getExpr().toString());
			printer.print(" + 1");
			break;
		case posDecrement:
			//printer.print("--");
			printer.print(n.getExpr().toString());
			printer.print(" = ");
			printer.print(n.getExpr().toString());
			printer.print(" - 1");
			break;
		}
	}

	public void visit(ConstructorDeclaration n, Object arg) {
		printJavadoc(n.getJavaDoc(), arg);
		printMemberAnnotations(n.getAnnotations(), arg);
		printer.print("method ");
		printer.print(n.getName());
		
		if (inAnonymousClass > 0) {
			anonymousMethod = n.getName();
		}
		
		printTypeParameters(n.getTypeParameters(), arg);
		if (n.getTypeParameters() != null) {
			printer.print(" ");
		}
	
		if (n.getParameters() != null) {
			printer.print("(");
			for (Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext();) {
				Parameter p = i.next();
				p.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
			printer.print(")");

		}

		printer.print(" ");
		printer.indent();
		printMethodVisibility(n.getModifiers());
		printMethodModifiers(n.getModifiers());
		printer.unindent();

		if (n.getThrows() != null) {
			printer.print(" signals ");
			for (Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext();) {
				NameExpr name = i.next();
				name.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.printLn();
		n.getBlock().accept(this, arg);
	}

	public void visit(MethodDeclaration n, Object arg) {
		printJavadoc(n.getJavaDoc(), arg);
		printMemberAnnotations(n.getAnnotations(), arg);
		printer.print("method ");
		printer.print(n.getName());

		printTypeParameters(n.getTypeParameters(), arg);
		if (n.getTypeParameters() != null) {
			printer.print(" ");
		}

		if (n.getParameters() != null) {
			printer.print("(");
			for (Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext();) {
				Parameter p = i.next();
				p.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
			printer.print(" )");
		}

		printer.print(" ");
		printer.indent();

		printMethodVisibility(n.getModifiers());
		printMethodModifiers(n.getModifiers());
		printer.unindent();

		if (!n.getType().getClass().getName().equals("japa.parser.ast.type.VoidType")) {
			printer.print(" returns ");
			n.getType().accept(this, arg);
		}

		for (int i = 0; i < n.getArrayCount(); i++) {
			printer.print("[]");
		}

		if (n.getThrows() != null) {
			printer.print(" signals ");
			for (Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext();) {
				NameExpr name = i.next();
				name.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.printLn();
		if (n.getBody() == null) {
			// printer.print(";");
		} else {
			// printer.print(" ");
			n.getBody().accept(this, arg);
		}
	}

	public void visit(Parameter n, Object arg) {
		printAnnotations(n.getAnnotations(), arg);
		printModifiers(n.getModifiers());

		if (n.isVarArgs()) {
			printer.print("/* **nonrx**  ... ** */");
		}
		printer.print(" ");
		
//ORIG 	n.getId().accept(this, arg);
//		printer.print("=");
//		n.getType().accept(this, arg);

		VariableDeclaratorId id = n.getId();
		printer.print(id.getName());
		
		printer.print("=");
		n.getType().accept(this, arg);
		
		if (id.getArrayCount() > 0) {
			printer.print("[");
			for (int i = 1; i < id.getArrayCount(); i++) {
				printer.print(",");
			}
			printer.print("]");			
		}
	}

	public void visit(ExplicitConstructorInvocationStmt n, Object arg) {
		if (n.isThis()) {
			printTypeArgs(n.getTypeArgs(), arg);
			printer.print("this");
		} else {
			if (n.getExpr() != null) {
				n.getExpr().accept(this, arg);
				printer.print(".");
			}
			printTypeArgs(n.getTypeArgs(), arg);
			printer.print("super");
		}
		printArguments(n.getArgs(), arg);
		// printer.print(";");
	}

	public void visit(VariableDeclarationExpr n, Object arg) {
		printAnnotations(n.getAnnotations(), arg);
		printModifiers(n.getModifiers());

		// ClassOrInterfaceType t = n.getType().toString();

		// ClassOrInterfaceType t = (ClassOrInterfaceType) n.getType();

//		printer.print(" XXX ");

		for (Iterator<VariableDeclarator> i = n.getVars().iterator(); i
				.hasNext();) {
			VariableDeclarator v = i.next();
			printer.print(v.getId().getName());
			printer.print(" = ");
			printer.print(n.getType().toString());
			printer.printLn();

			if (v.getInit() != null) {
				// printer.printLn();
				printer.print(v.getId().getName());
				printer.print(" = ");
				v.getInit().accept(this, arg);
			} else {
				// printer.print(v.getId().getName());
				// printer.print(" = ");
				// printer.print(n.getType().toString());
			}

			if (i.hasNext()) {
				printer.printLn();
			}
		}
	}

	public void visit(TypeDeclarationStmt n, Object arg) {
		n.getTypeDeclaration().accept(this, arg);
	}

	public void visit(AssertStmt n, Object arg) {
		printer.print("assert ");
		n.getCheck().accept(this, arg);
		if (n.getMessage() != null) {
			printer.print(" : ");
			n.getMessage().accept(this, arg);
		}
		// printer.print(";");
	}

	public void visit(BlockStmt n, Object arg) {
		// printer.printLn("do");
		if (n.getStmts() != null) {
			printer.indent();
			for (Statement s : n.getStmts()) {
				s.accept(this, arg);
				printer.printLn();
			}
			printer.unindent();
		}
		// printer.print("end");

	}

	public void visit(LabeledStmt n, Object arg) {
		printer.print(n.getLabel());
		printer.print(": ");
		n.getStmt().accept(this, arg);
	}

	public void visit(EmptyStmt n, Object arg) {
		// printer.print(";");
	}

	public void visit(ExpressionStmt n, Object arg) {
		n.getExpression().accept(this, arg);
		// printer.print(";");
	}

	public void visit(SwitchStmt n, Object arg) {

		if (n.getEntries() != null) {
			printer.printLn();
			printer.print("select");
			printer.printLn();
			boolean emptyWhen = false;

			for (SwitchEntryStmt e : n.getEntries()) {
				if (e.getLabel() != null) {
					if (!emptyWhen) {
						printer.print("when ");
						emptyWhen = false;
					}
					printer.print(n.getSelector().toString());
					printer.print(" = ");
					printer.print(e.getLabel().toString());
					// e.accept(this,arg);
					if (e.getStmts() != null) {
						printer.printLn(" then");
						printer.printLn("do");
					} else {
						printer.print(" | ");
						emptyWhen = true;
					}
				} else {
					printer.printLn("otherwise");
				}
				if (e.getStmts() != null) {
					printer.indent();
					for (Statement s : e.getStmts()) {
						s.accept(this, arg);
						printer.printLn();
					}
					printer.unindent();
					printer.printLn(" end ");
				}
				// if (e.getLabel() != null) {

				// }
			}
		}
		// printer.printLn("end");

	}

	public void visit(SwitchEntryStmt n, Object arg) {
		if (n.getLabel() != null) {
			// printer.print("case ");
			n.getLabel().accept(this, arg);
			// printer.print(":");
		} else {
			printer.print("default:");
		}
		printer.printLn();
		printer.indent();
		if (n.getStmts() != null) {
			for (Statement s : n.getStmts()) {
				s.accept(this, arg);
				printer.printLn();
			}
		}
		printer.unindent();
	}

	public void visit(BreakStmt n, Object arg) {
		printer.print("leave");
		if (n.getId() != null) {
			printer.print(" ");
			printer.print(n.getId());
		}
		// printer.print(";");
	}

	public void visit(ReturnStmt n, Object arg) {
		printer.print("return");
		if (n.getExpr() != null) {
			printer.print(" ");
			n.getExpr().accept(this, arg);
		}
		// printer.print(";");
	}

	public void visit(EnumDeclaration n, Object arg) {
		printJavadoc(n.getJavaDoc(), arg);
		printMemberAnnotations(n.getAnnotations(), arg);
		printModifiers(n.getModifiers());

		printer.print("/* **nonrx**  enum ");
		printer.print(n.getName());

		if (n.getImplements() != null) {
			printer.print(" implements ");
			for (Iterator<ClassOrInterfaceType> i = n.getImplements()
					.iterator(); i.hasNext();) {
				ClassOrInterfaceType c = i.next();
				c.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}

		printer.printLn(" {");
		printer.indent();
		if (n.getEntries() != null) {
			printer.printLn();
			for (Iterator<EnumConstantDeclaration> i = n.getEntries()
					.iterator(); i.hasNext();) {
				EnumConstantDeclaration e = i.next();
				e.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		if (n.getMembers() != null) {
			printer.printLn(";");
			printMembers(n.getMembers(), arg);
		} else {
			if (n.getEntries() != null) {
				printer.printLn();
			}
		}
		printer.unindent();
		printer.print("} ** */");
	}

	public void visit(EnumConstantDeclaration n, Object arg) {
		printJavadoc(n.getJavaDoc(), arg);
		printMemberAnnotations(n.getAnnotations(), arg);
		printer.print(n.getName());

		if (n.getArgs() != null) {
			printArguments(n.getArgs(), arg);
		}

		if (n.getClassBody() != null) {
			printer.printLn(" {");
			printer.indent();
			printMembers(n.getClassBody(), arg);
			printer.unindent();
			printer.printLn("}");
		}
	}

	public void visit(EmptyMemberDeclaration n, Object arg) {
		printJavadoc(n.getJavaDoc(), arg);
		// printer.print(";");
	}

	public void visit(InitializerDeclaration n, Object arg) {
		printJavadoc(n.getJavaDoc(), arg);
		if (n.isStatic()) {
			printer.print("static ");
		}
		n.getBlock().accept(this, arg);
	}

	public void visit(IfStmt n, Object arg) {
		printer.print("if ");
		n.getCondition().accept(this, arg);
		printer.print(" then do ");
		printer.printLn();
		if (n.getCondition().getBeginLine() == n.getThenStmt().getBeginLine()) {
			printer.indent();
		}
		n.getThenStmt().accept(this, arg);
		
		if (n.getCondition().getBeginLine() == n.getThenStmt().getBeginLine()) {
			printer.printLn();
			printer.unindent();
		} else {
   		printer.printLn(" ");
		}	
		printer.printLn(" end ");

		if (n.getElseStmt() != null) {
			if (n.getThenStmt().getBeginLine() == n.getElseStmt().getBeginLine()) {
				printer.indent();
			}
			printer.printLn("else do ");
			n.getElseStmt().accept(this, arg);
			
			if (n.getThenStmt().getBeginLine() == n.getElseStmt().getBeginLine()) {
				printer.printLn();
				printer.unindent();
			} else {
				printer.printLn(" ");
			}	

			printer.printLn(" end ");
		}
	}

	public void visit(WhileStmt n, Object arg) {
		printer.print("loop while ");
		// n.getCondition().toString();
		n.getCondition().accept(this, arg);

		printer.printLn();
		n.getBody().accept(this, arg);

		printer.printLn();
		printer.print(" end ");

	}

	public void visit(ContinueStmt n, Object arg) {
		printer.print("continue");
		if (n.getId() != null) {
			printer.print(" ");
			printer.print(n.getId());
		}
		// printer.print(";");
	}

	public void visit(DoStmt n, Object arg) {
		printer.print("while ");
		n.getCondition().accept(this, arg);
		printer.printLn(" ");
		n.getBody().accept(this, arg);
		printer.print(" end ");

	}

	public void visit(ForeachStmt n, Object arg) {
		printer.print("for (");
		n.getVariable().accept(this, arg);
		printer.print(" : ");
		n.getIterable().accept(this, arg);
		printer.print(") ");
		n.getBody().accept(this, arg);
	}

	public void visit(ForStmt n, Object arg) {
		/*
		 * printer.print("for ("); if (n.getInit() != null) { for
		 * (Iterator<Expression> i = n.getInit().iterator(); i.hasNext();) {
		 * Expression e = i.next(); e.accept(this, arg); if (i.hasNext()) {
		 * printer.print(", "); } } } printer.print("; "); if (n.getCompare() !=
		 * null) { n.getCompare().accept(this, arg); } printer.print("; "); if
		 * (n.getUpdate() != null) { for (Iterator<Expression> i =
		 * n.getUpdate().iterator(); i.hasNext();) { Expression e = i.next();
		 * e.accept(this, arg); if (i.hasNext()) { printer.print(", "); } } }
		 * printer.print(") "); n.getBody().accept(this, arg);
		 */
//		printer.print("loop ");
		if (n.getInit() != null) {
			for (Iterator<Expression> i = n.getInit().iterator(); i.hasNext();) {
				Expression e = i.next();
				e.accept(this, arg);
				if (i.hasNext()) {
					printer.printLn();
				}
			}
		}
		printer.printLn();
		printer.print("loop ");

		if (n.getCompare() != null) {
			printer.print("while ");
			n.getCompare().accept(this, arg);
		}
		
		printer.printLn();
		n.getBody().accept(this, arg);

		if (n.getUpdate() != null) {
			printer.indent();
			printer.printLn();
			for (Iterator<Expression> i = n.getUpdate().iterator(); i.hasNext();) {
				Expression e = i.next();

				e.accept(this, arg);
				if (i.hasNext()) {
					printer.printLn();
				}
			}
		}
		printer.unindent();
		printer.printLn();
		printer.printLn(" end ");

	}

	public void visit(ThrowStmt n, Object arg) {
		printer.print("signal ");
		n.getExpr().accept(this, arg);
		// printer.print(";");
	}

	public void visit(SynchronizedStmt n, Object arg) {
		printer.print("/* **nonrx**  synchronized ( ** */");
		n.getExpr().accept(this, arg);
		printer.print("/* **nonrx**  ) ** */");
		n.getBlock().accept(this, arg);
	}

	public void visit(TryStmt n, Object arg) {
		printer.printLn("do ");
		n.getTryBlock().accept(this, arg);
		// n.getTryBlock().getStmts()
		if (n.getCatchs() != null) {
			for (CatchClause c : n.getCatchs()) {
				c.accept(this, arg);
			}
		}
		if (n.getFinallyBlock() != null) {
			printer.printLn("finally ");
			n.getFinallyBlock().accept(this, arg);
		}
		printer.print(" end ");
	}

	public void visit(CatchClause n, Object arg) {
		printer.print(" catch ");
		n.getExcept().accept(this, arg);
		printer.printLn();
		n.getCatchBlock().accept(this, arg);

	}

	public void visit(AnnotationDeclaration n, Object arg) {
		printJavadoc(n.getJavaDoc(), arg);
		printMemberAnnotations(n.getAnnotations(), arg);
		printModifiers(n.getModifiers());

		printer.print("/* **nonrx**  @interface ");
		printer.print(n.getName());
		printer.printLn(" {");
		printer.indent();
		if (n.getMembers() != null) {
			printMembers(n.getMembers(), arg);
		}
		printer.unindent();
		printer.print("} ** */");
	}

	public void visit(AnnotationMemberDeclaration n, Object arg) {
		printJavadoc(n.getJavaDoc(), arg);
		printMemberAnnotations(n.getAnnotations(), arg);
		printModifiers(n.getModifiers());

		n.getType().accept(this, arg);
		printer.print(" ");
		printer.print(n.getName());
		printer.print("()");
		if (n.getDefaultValue() != null) {
			printer.print(" default ");
			n.getDefaultValue().accept(this, arg);
		}
	}

	public void visit(MarkerAnnotationExpr n, Object arg) {
		printer.print("@");
		n.getName().accept(this, arg);
	}

	public void visit(SingleMemberAnnotationExpr n, Object arg) {
		printer.print("@");

		n.getName().accept(this, arg);
		printer.print("(");
		n.getMemberValue().accept(this, arg);
		printer.print(")");

	}

	public void visit(NormalAnnotationExpr n, Object arg) {
		printer.print("@");

		n.getName().accept(this, arg);
		printer.print("(");
		if (n.getPairs() != null) {
			for (Iterator<MemberValuePair> i = n.getPairs().iterator(); i
					.hasNext();) {
				MemberValuePair m = i.next();
				m.accept(this, arg);
				if (i.hasNext()) {
					printer.print(", ");
				}
			}
		}
		printer.print(")");

	}

	public void visit(MemberValuePair n, Object arg) {
		printer.print(n.getName());
		printer.print(" = ");
		n.getValue().accept(this, arg);
	}

	public void visit(LineComment n, Object arg) {
		printer.print("--");
		printer.printLn(n.getContent());
	}

	public void visit(BlockComment n, Object arg) {
		printer.print("/*");
		printer.print(n.getContent());
		printer.printLn("*/");
	}

}
