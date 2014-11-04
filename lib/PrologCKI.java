/**
 * CKI Prolog is an prolog interpreter
 * Sieuwert van Otterloo
 * http://www.students.cs.uu.nl/~smotterl/prolog
 * smotterl@cs.uu.nl
 * 1999
 */

/**
 * Note: from on the web site
 * http://www.students.cs.uu.nl/~smotterl/prolog
 * is a message:
 * "The sourcecode of CKI prolog is free. This means
 * that it can be modified by anyone."
 *
 * In addition, I received specific permission from
 * Sieuwert van Otterloo use this code for an example
 * for my Java AI book.  -Mark Watson
 */

/**
 * Note #2: Sieuwert's original code was a nice
 * Java applet with a user interface for Prolog.
 * I removed the user interface code and added an
 * API (top level Prolog class) for using the
 * Prolog engine in Java applications. -Mark Watson
 */

import java.util.*;
import java.io.*;

/**********************************************************
 * PROLOG
 **********************************************************/

public class Prolog {
    program prog;
    solver sv;
    static private boolean once = true;

    public Prolog() {
        prologop.makeops();
        if (once) {
            System.out.println("CKI Prolog Engine. By Sieuwert van Otterloo.\n");
            once = false;
        }
        prog=new program();
        sv=new solver(prog);
        prog.setsolver(sv);
    }
    public void assert(String s) {
        term inp=(new prologtokenizer(s)).gettermdot(null);
        if (sv.assert(inp) == false) {
            System.out.println("Error asserting: " + s);
        }
    }
    public Vector solve(String s) {
        term inp=(new prologtokenizer(s)).gettermdot(null);
        sv.query(inp);
        return sv.getAnswers();
    }
    public boolean consultFile(String filename) {
	try {
	    FileReader fr = new FileReader(filename);
	    BufferedReader br = new BufferedReader(fr);
	    int count = 0;
	    while (true) {
		String line = br.readLine();
		if (line == null) break;
		System.out.println(line);
		line = line.trim();
		if (line.startsWith("%") || line.length() < 1) continue;
		if (line.endsWith(".")) {
		    assert(line);
		} else {
		    while (line.endsWith(".") == false) {
			String s2 = br.readLine();
			System.out.println(s2);
			line = line + " " + s2;
		    }
		    assert(line);
		}
	    }
	    br.close();
	    return true; // OK
	} catch (Exception e) {
	    System.out.println("consultFile error: " + e);
	    e.printStackTrace();
	}
	return false;
    }

    static public void main(String [] args) {
	if (args.length == 0) {
	    /**
	     * Simple example howing how to use embedded Prolog
	     * in your Java programs:
	     */
	    Prolog p = new Prolog();
	    p.assert("father(ken,mark).");
	    p.assert("father(ken,ron).");
	    p.assert("father(ron,anthony).");
	    p.assert("grandfather(X,Z):-father(X,Y),father(Y,Z).");
	    Vector v = p.solve("grandfather(X,Y).");
	    System.out.println("test results:");
	    //      Vector v = p.solve("permutation([1,2,3],X).");
	    for (int i=0; i<v.size(); i++) {
		System.out.println();
		Hashtable the_answers = (Hashtable)v.elementAt(i);
		Enumeration enum = the_answers.keys();
		while (enum.hasMoreElements()) {
		    String var = (String)enum.nextElement();
		    String val = (String)the_answers.get(var);
		    System.out.println(" var: " + var + "   val: " + val);
		}
	    }
	} else if (args.length > 1) {
	    /**
	     * Two arguments: a prolog file and a query
	     */
	    try {
		Prolog p = new Prolog();
		p.consultFile(args[0]);
		Vector v = p.solve(args[1]);
		for (int i=0; i<v.size(); i++) {
		    System.out.println("\nNext answer:");
		    Hashtable the_answers = (Hashtable)v.elementAt(i);
		    Enumeration enum = the_answers.keys();
		    while (enum.hasMoreElements()) {
			String var = (String)enum.nextElement();
			String val = (String)the_answers.get(var);
			System.out.println(" var: " + var + "   val: " + val);
		    }
		}
	    } catch (Exception e) {
		System.out.println("error: " + e);
		e.printStackTrace();
	    }
	}
    }
}


/*The prologtokenizer breaks a string in such pieces that are usefull
for parsing. It searches for constants, numbers, or special operators,
and else it returns the characters one by one.
It ignores spaces, tabs,enters and %comment.*/

class prologtokenizer {
    String unused;/*the unprocessed part of the string*/
    Vector tokens;/*the allready extracted tokens*/
    int cursor;/*the current position in the token vector.*/

    static String normalchar=
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz1234567890";
    static String opchar="+-*/\\^<>=`~:.?@#$&";
    static String numchar="1234567890";

    boolean splitoff() {
        /*extract a token from unused, and adds it to tokens*/
        /*step 1: skip whitespace
          2: decide on first character what the token kind is.
          3: seek the end of the token (start)
          4: shorten unused, add the token, return true;*/
        if(unused==null)            return false;
        int max=unused.length();
        int start=0;
        boolean comment=false;
        for(start=0;start<max;start++) {
            if(unused.charAt(start)=='%')
                comment=true;
            else if(unused.charAt(start)=='\n')
                comment=false;
            if(!comment&&unused.charAt(start)>32)
                break;
        }
        if(start==max) {
            unused=null;return false;
        }
        StringBuffer buf=new StringBuffer();
        char d;
        char c=unused.charAt(start);
        start++;
        buf.append(c);
        if(c==39||c=='"') {
            boolean closed=false;
            for(;start<max;start++) {
                d=unused.charAt(start);
                if(d==c) {
                    start++;
                    if(start<max&&unused.charAt(start)==c);//mind the ;
                    else
                        {closed=true;break;}
                }
                buf.append(d);
            }
            if(!closed)
                return false;
        }
        else
            if(c=='\"') {
                boolean closed=false;
                for(;start<max;start++) {
                    d=unused.charAt(start);
                    if(d==c) {
                        start++;
                        if(start<max&&unused.charAt(start)==c);//mind the ;
                        else
                            {buf.append(d);closed=true;break;}
                    }
                    buf.append(d);
                }
            }
            else if(in(c,numchar)) { //number
                for(;start<max;start++) {
                    d=unused.charAt(start);
                    if(!in(unused.charAt(start),numchar))
                        break;
                    buf.append(d);
                }
            }  else if(in(c,opchar)) { //a special operator
                for(;start<max;start++) {
                    d=unused.charAt(start);
                    if(!in(unused.charAt(start),opchar))
                        break;
                    buf.append(d);
                }
            } else if(in(c,normalchar)) {//normal constant
                for(;start<max;start++) {
                    d=unused.charAt(start);
                    if(!in(unused.charAt(start),normalchar))
                        break;
                    buf.append(d);
                }
            }

        tokens.addElement(buf.toString());
        unused=unused.substring(start);
        return true;
    }

    public prologtokenizer(String s) {
        unused=s;
        cursor=0;
        tokens=new Vector();
    }

    public term gettermdot(Thread t) {
        /*get a term, closed by a . (dot or period).*/
        term t1=term.getTerm(this);
        if(t1!=null&&".".equals(gettoken()))
            return t1;
        return null;
    }

    char get0() {
        /*get a single character.*/
        char c='*';
        if(unused!=null&&unused.length()>0) {
            c=unused.charAt(0);
            unused=unused.substring(1);
        }
        return c;
    }

    public boolean more() {
        /*do we have more tokens?*/
        if(cursor<tokens.size())
            return true;
        return splitoff();
    }
    public String peek() {
        /*returns the first token, but does not remove it.*/
        if(cursor>tokens.size())
            return null;
        if(cursor==tokens.size())
            if(!splitoff())return null;
        return (String)tokens.elementAt(cursor);
    }
    public String gettoken() {
        /*removes a token out of this tokenizer, and returns that token*/
        if(!more())
            return null;
        cursor++;
        return (String)tokens.elementAt(cursor-1);
    }
    int getpos()/*return the position in the tokenvector*/
    {return cursor;}
    void jumpto(int i)/*jump to a position in the tokenvector*/
    {cursor=i;}

    static boolean in(char c,String s) {
        /*tells wether a char is in a string.*/
        for(int i=s.length()-1;i>=0;i--)
            if(c==s.charAt(i))
                return true;
        return false;
    }
}

/**********************************************************
 *  T E R M
 **********************************************************/

class term {
    int type;
    public final static int EQ=211,OPEN=212,NUMBER=213,FUNCTOR=214;
    String name;//the functor or constant name.
    String varname;//the name this term would have as a variable
    String qname;/*the name with quotes, if necessary.*/
    int arity;//the number of arguments in a functor.
    term[] arg;//the arguments in case of functor;

    public final static int MAXARG=12;//the maximum number of arguments
    static term emptylist;//the unique empty list
    static String varstart="_ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static String normalstart=
        "abcdefghijklmnopqrstuvwxyz'+-*/\\^<>=`~:.?@#$&";
    static String numchar="1234567890";
    static int NaN=Integer.MIN_VALUE;/*Not a Number*/

    term() { /*make anonymous variable*/
        type=OPEN;
        arity=0;
    }
    term(String s) { /*named variable*/
        type=OPEN;
        arity=0;
        varname=s;
    }
    term(prologop pre1,term t) { /*unary operator*/
        type=FUNCTOR;
        name=pre1.name;
        qname=getqname(name);
        arity=0;
        addarg(t);
    }
    static term newconstant(String n)
    {return newconstant(n,getqname(n));}
    static term newconstant(String n,String qn) {
        /*make a constant (for example abc)*/
        term t=new term();
        t.type=FUNCTOR;
        t.name=n;
        t.qname=qn;
        return t;
    }

    static String getqname(String inp)
    /*decides wether a name should be quoted.*/
    {if(inp.length()!=0&&
        prologtokenizer.in(inp.charAt(0),normalstart))  {
        boolean simple1=true,simple2=true;
        for(int i=0;i<inp.length();i++)
            if(!prologtokenizer.in(inp.charAt(i),
                                   prologtokenizer.normalchar))
                {simple1=false;break;}
        if(!simple1)
            for(int i=0;i<inp.length();i++)
                if(!prologtokenizer.in(inp.charAt(i),
                                       prologtokenizer.opchar))
                    {simple2=false;break;}
        if(simple1||simple2)
            return inp;
    }
    return "'"+inp+"'";
    }

    term(term t,prologop in1,term t2) { /*infix operator*/
        type=FUNCTOR;
        name=in1.name;
        qname=getqname(name);
        arity=0;
        addarg(t);
        addarg(t2);
    }
    term(int n) { /*number*/
        type=NUMBER;
        arity=n;
    }

    static term asciilist(String s) {
        /*make a list of asciivalues*/
        term t=emptylist;
        for(int i=s.length()-1;i>=0;i--)
            t=makelist(new term((int)s.charAt(i)),t);
        return t;
    }

    static String readasciilist(term t) {
        /*make a string from a list of asciivalues*/
        StringBuffer buf=new StringBuffer();
        term num;
        t=skipeq(t);
        while(t.name!=emptylist.name) {
            if(t.type!=t.FUNCTOR||t.name!=prologop.listcons.name)
                return null;
            num=skipeq(t.arg[0]);
            if(num.type!=NUMBER||num.arity<0||num.arity>255)
                return null;
            buf.append((char)num.arity);
            t=skipeq(t.arg[1]);
        }
        return buf.toString();
    }

    static term makelist(term head,term tail)
    {return new term(head,prologop.listcons,tail);}

    void functor(String s)/*make this term a functor*/
    {functor(s,getqname(s));}
    void functor(String s,String qs) { /*make this term a functor*/
        type=FUNCTOR;
        name=s;
        qname=qs;
        arity=0;
    }

    void addarg(term g) { /*add an argument to this functor*/
        if(arg==null)
            arg=new term[MAXARG];
        arg[arity]=g;
        arity++;
    }

    static void is(term x,term y) {
        /*instatiate X to being the same as Y. */
        if(x==y)
            return;
        x.arity=0;
        x.type=EQ;
        x.addarg(y);
    }

    static term skipeq(term t)
    {
        while(t.type==EQ)
            t=t.arg[0];
        return t;
    }
    static boolean equal(term a,term b) {
        a=skipeq(a);
        b=skipeq(b);
        if(a.type!=b.type)
            return false;
        if(a.type==NUMBER)
            return a.arity==b.arity;
        if(a.type==OPEN)
            return a==b;
        if(!a.name.equals(b.name)||a.arity!=b.arity)
            return false;
        for(int i=0;i<a.arity;i++)
            if(!equal(a.arg[i],b.arg[i]))
                return false;
        return true;
    }

    static boolean match(term in1,term in2, Stack substitutions) {
        /*match to variables. all variables that are instantiated
          are added to substitutions.*/
        Stack s=new Stack();
        Stack t=new Stack();
        term top1,top2;
        s.push(in1);
        t.push(in2);
        int height=substitutions.size();
        /*Instead of stacking pairs I have a pair of stacks. Both
          stacks will have the same number of elements.*/
        while(!s.empty()) {
            top1=skipeq((term)s.pop());
            top2=skipeq((term)t.pop());
            if(top1.type==OPEN) {
                is(top1,top2);
                substitutions.push(top1);
            } else if(top2.type==OPEN) {
                is(top2,top1);
                substitutions.push(top2);
            } else if(top1.type!=top2.type) {
                unmatch(substitutions,height);
                return false;
            } else if(top1.type==NUMBER) {
                if(top1.arity!=top2.arity)
                    { unmatch(substitutions,height);
                    return false;
                    }
            }
            else if(top1.arity!=top2.arity||!top1.name.equals(top2.name)) {
                unmatch(substitutions,height);
                return false;
            } else for(int i=0;i<top1.arity;i++) {
                s.push(top1.arg[i]);
                t.push(top2.arg[i]);
            }
        }
        return true;
    }

    void open() { /*Make this term an open variable*/
        type=OPEN;
        arity=0;
        arg=null;
    }

    static void unmatch(Stack subst,int height) {
        /*bring subst to the given height. Undo the instantations*/
        while(subst.size()>height)
            ((term)subst.pop()).open();
    }

    static int numbervalue(term t) {
        /*calculate the number represented by a term.
          the term must consist of NUMBER constants kept together
          by +,-,*,/,mod.*/
        t=skipeq(t);
        if(t.type==NUMBER)
            return t.arity;
        if(t.type!=FUNCTOR||t.arity==0)
            return NaN;
        int a1=numbervalue(t.arg[0]);
        if(a1==NaN)
            return NaN;
        if(t.arity==1) {
            if(t.name.equals("-"))
                return -a1;
            if(t.name.equals("+"))
                return  a1;
        }
        if(t.arity!=2)  return NaN;
        int a2=numbervalue(t.arg[1]);
        if(a2==NaN)     return NaN;
        if(t.name.equals("+"))    return a1+a2;
        if(t.name.equals("*"))    return a1*a2;
        if(t.name.equals("-"))    return a1-a2;
        if(t.name.equals("/")) {
            if(a2==0)   return NaN;
            return a1/a2;
        }
        if(t.name.equals("mod")) {
            if(a2==0)  return NaN;
            return a1%a2;
        }
        return NaN;
    }

    /*displaying terms as strings:*/

    /*ebbinghaus makes nice names for new variables.*/
    static String[] vowel={"a","u","i","o","e"};
    static String[] conso1={"B","D","F","G","H","K","L","M","N","Z","X"};
    static String[] conso2={"","g","f","l","n","m","s","p","t"};
    static int count=0;
    static int total=0;

    static String ebbinghaus() {
        /*return a nonsense syllable as varname*/
        if(total==0)
            total=conso1.length*vowel.length*conso2.length;
        count++;
        return   conso1[count%conso1.length]+
            vowel[count%vowel.length]+
            conso2[count%conso2.length];
    }
    String varname() {
        /*the name of this term if it were a variable*/
        if(varname==null)
            varname=ebbinghaus();
        return varname;
    }

    static String tailstring(term t,boolean quotes) {
        /*write a term as the end of a string (without [)*/
        t=skipeq(t);
        if(t.type==FUNCTOR)
            {if(t.name==emptylist.name)
                return "]";
            if(t.name==prologop.listcons.name)
                return ","+toString(t.arg[0],1000,quotes)
                    +tailstring(t.arg[1],quotes);
            }
        return "|"+t.toString()+"]";
    }

    static String toString(term t,int level,boolean q) {
        /*write a term as a string.*/
        t=skipeq(t);
        switch(t.type) {
        case NUMBER:
            return t.arity+"";
        case FUNCTOR:
            if(t.name==prologop.listcons.name)/*try list display*/
                return "["+toString(t.arg[0],999,q)+tailstring(t.arg[1],q);
            if(t.arity==0)
                if(q)
                    return t.qname;
                else
                    return t.name;
            prologop o1=prologop.preop(t.name);/*try prefix operator*/
            if(t.arity==1&&o1!=null)
                {    if(o1.priority<=level)
                    return t.name+" "+
                        toString(t.arg[0],o1.rightunderlevel(),q);
                return "("+t.name+" "+
                    toString(t.arg[0],o1.rightunderlevel(),q)+")";
                }
            o1=prologop.postop(t.name);/*try postfix*/
            if(t.arity==1&&o1!=null) {
                if(o1.priority<=level)
                    return toString(t.arg[0],o1.leftunderlevel(),q)
                        +" "+t.name;
                return "("+toString(t.arg[0],o1.leftunderlevel(),q)
                    +" "+t.name+")";
            }
            o1=prologop.inop(t.name);/*try infix*/
            if(t.arity==2&&o1!=null) {
                String s=toString(t.arg[0],o1.leftunderlevel(),q)+
                    " "+t.name+" "+
                    toString(t.arg[1],o1.rightunderlevel(),q);
                if(o1.priority<=level)
                    return s;
                return "("+s+")";
            }
            String s;
            if(q)
                s=t.qname;
            else
                s=t.name;
            s+="("+t.toString(t.arg[0],999,q);
            for(int i=1;i<t.arity;i++)
                s+=","+toString(t.arg[i],999,q);
            return s+")";
        case OPEN:
            return t.varname();
        }
        return null;
    }
    public String toString()
    {return toString(this,1201,true);}
    public String toString(boolean q)
    {return toString(this,1201,q);}

    /************** P A R S I N G *************/

    public static term getTerm(prologtokenizer tok)
    /*get a term out of a tokenizer.*/
    {return parset(tok,new Hashtable(),1200);}

    static term parset(prologtokenizer tok,Hashtable vars, int level) {
        /*parse a tokenized string to a term, with operatorlevel<=level
          The hashtable contains the variables that are allready made. */
        if(!tok.more())
            return null;
        int tokpos=tok.getpos();

        prologop p1=prologop.preop(tok.gettoken());
        if(p1!=null&&p1.priority<=level) {
            term t1=parset(tok,vars,p1.rightunderlevel());
            if(t1!=null) {
                t1=readfurther(new term(p1,t1),p1.priority,tok,vars,level);
                if(t1!=null)
                    return t1;
            }
        }/*:the paring of [prefixop][term]. (like -4)*/

        tok.jumpto(tokpos);
        term t=parsetbasic(tok,vars);
        if(t!=null) {
            t=readfurther(t,0,tok,vars,level);
            if(t!=null)
                return t;
        }/*:the parsing of [term]*/
        tok.jumpto(tokpos);
        return null;
    }

    static term readfurther(term t1,int t1level,
                            prologtokenizer tok,Hashtable vars,int level) {
        /*try to add postfix and infix operators*/
        int tokpos=tok.getpos();
        term t;
        if(!tok.more())
            return t1;
        prologop p1=prologop.postop(tok.gettoken());
        if(p1!=null&&p1.priority<=level&&t1level<p1.leftunderlevel())  {
            t=readfurther(new term(p1,t1),p1.priority,
                          tok,vars,level);
            if(t!=null)
                return t;
        }
        tok.jumpto(tokpos);
        p1=prologop.inop(tok.gettoken());
        if(p1!=null&&p1.priority<=level&&t1level<p1.leftunderlevel()) {
            term t2=parset(tok,vars,p1.rightunderlevel());
            if(t2!=null) {
                t=fixin(t1,p1,t2,tok,vars,level);
                if(t!=null)
                    return t;
            }
        }
        tok.jumpto(tokpos);
        return t1;/*don't take next operator, wrong level.*/
    }

    static term fixin(term t1, prologop o1,term t2,
                      prologtokenizer tok,Hashtable vars,int highlevel)  {
        if(!tok.more())
            return new term(t1,o1,t2);
        int tokpos=tok.getpos();
        term t;
        prologop o2=prologop.inop(tok.gettoken());
        if(o2!=null&&o2.priority<=highlevel) {
            term t3=parset(tok,vars,o2.rightunderlevel());
            if(t3!=null) {
                if(o1.under(o1,o2)==1) {
                    t=fixin(new term(t1,o1,t2),o2,t3,tok,vars,highlevel);
                    if(t!=null)
                        return t;
                } else if(o1.under(o1,o2)==2)
                    return new term(t1,o1,new term(t2,o2,t3));
                //fail: operators cannot be combined
            }//if t3 is null: fail.
        } else { /*there is no or a too high operator. succeed*/
            tok.jumpto(tokpos);
            return new term(t1,o1,t2);
        }
        tok.jumpto(tokpos);
        return null;
    }

    static term listread(prologtokenizer tok,Hashtable vars) {
        /*listread transforms a tokenized string  3,4,5] to a list*/
        int tokpos=tok.getpos();
        term head=parset(tok,vars,999);
        if(head==null) {
            tok.jumpto(tokpos);
            return null;
        }
        int afterhead=tok.getpos();
        if("]".equals(tok.gettoken()))
            return makelist(head,emptylist);
        tok.jumpto(afterhead);
        if(",".equals(tok.gettoken())) {
            term tail=listread(tok,vars);
            if(tail==null)
                {tok.jumpto(tokpos);
                return null;
                }
            return makelist(head,tail);
        }
        tok.jumpto(afterhead);
        if("|".equals(tok.gettoken())) {
            term tail=parset(tok,vars,699);/*under =*/
            if(tail!=null&&"]".equals(tok.gettoken()))
                return makelist(head,tail);
        }
        tok.jumpto(tokpos);
        return null;
    }

    static term parsetbasic(prologtokenizer tok,Hashtable vars) {
        /*null-pointer indicates failure.*/
        term t;
        if(!tok.more())
            return null;
        int tokpos=tok.getpos();
        String f1=tok.peek();
        char first=f1.charAt(0);
        if(f1.equals("!")) {
            tok.gettoken();
            t=newconstant(prologop.CUT,prologop.CUT);
            return t;
        }
        if(f1.equals("(")) {
            tok.gettoken();
            t=parset(tok,vars,1200);
            if(")".equals(tok.gettoken()))
                return t;
            else {
                tok.jumpto(tokpos);
                return null;
            }
        }
        if(f1.equals("[")) {
            tok.gettoken();
            if("]".equals(tok.peek())) {
                tok.gettoken();
                return emptylist;
            }
            return listread(tok,vars);
        }
        if(first=='"') {
            tok.gettoken();
            return asciilist(f1.substring(1));
        }

        if(tok.in(first,varstart)) {
            tok.gettoken();
            term old=(term)vars.get(f1);
            if(old!=null&&!f1.equals("_"))
                return old;
            t=new term(f1);
            vars.put(f1,t);
            return t;
        }
        else if(tok.in(first,numchar)) {
            int n;
            try{n=Integer.parseInt(tok.gettoken());}
            catch(NumberFormatException e)
                {return null;}
            return new term(n);
        }
        else if(tok.in(first,normalstart)) {
            tok.gettoken();
            t=new term();
            if(first==39)
                t.functor(f1.substring(1));
            else
                t.functor(f1);
            if("(".equals(tok.peek())) {  //try adding arguments
                tok.gettoken();//get the (
                for(int arc=0;arc<MAXARG;arc++) {
                    term q=parset(tok,vars,999);/*under , */
                    if(q==null) return null;//failure
                    t.addarg(q);
                    if(")".equals(tok.peek())) {
                        tok.gettoken();
                        return t;
                    }
                    if(!",".equals(tok.gettoken())) {
                        tok.jumpto(tokpos);
                        return null;
                    }
                }
            }
            return t;
        }
        tok.jumpto(tokpos);
        return null;
    }
    /*make a copy of a term:*/
    term copy()
    {return copy(new Hashtable());}

    term copy(Hashtable h) {
        term t;
        switch(type)
            {case EQ: return arg[0].copy();
            case NUMBER: return new term(arity);
            case OPEN:
                t=(term)h.get(this);
                if(t==null) {
                    t=new term();
                    h.put(this,t);
                }
                return t;
            case FUNCTOR:
                t=newconstant(name,qname);
                for(int i=0;i<arity;i++)
                    t.addarg(arg[i].copy(h));
                return t;
            }
        return null;
    }

    static void vars(term t,Vector v) {
        /*put all vars in term t in the vector*/
        t=skipeq(t);
        if(t.type==OPEN) {
            if(!v.contains(t))
                v.addElement(t);
        }
        else if(t.type==FUNCTOR)
            for(int i=0;i<t.arity;i++)
                vars(t.arg[i],v);
    }
}

/**********************************************************
 *  P R O L O G O P
 **********************************************************/

class prologop {
    boolean prex,postx;
    int place,priority;
    static int pre=1,in=2,post=3;
    String name;
    static String AND,OR,MATCH,ARROW,CUT,REWRITE;
    /*all operators in play are defined here:*/
    static Hashtable preops,inops,postops;
    static prologop listcons;

    prologop(){}/*empty constructor.
                  use make as a (sometimes failing) constructor*/

    static prologop make(String n,String type,int prior) {
        /*returns such an operator, or null*/
        if(prior<0||prior>1200)
            return null;
        prologop p=new prologop();
        p.name=n;
        p.priority=prior;
        if(type.length()==2&&type.charAt(0)=='f')
            {p.place=pre;
            if(type.equals("fx"))
                p.postx=true;
            else if(type.equals("fy"))
                p.postx=false;
            else
                return null;
            return p;
            }
        else if(type.length()==2&&type.charAt(1)=='f')
            {p.place=post;
            if(type.equals("xf"))
                p.prex=true;
            else if(type.equals("fy"))
                p.prex=false;
            else
                return null;
            return p;
            }
        else if(type.length()==3&&type.charAt(1)=='f') {
            p.place=in;
            if(type.equals("xfx")) {
                p.prex=true;
                p.postx=true;
            }
            else if(type.equals("xfy")) {
                p.prex=true;
                p.postx=false;
            }
            else if(type.equals("yfx")) {
                p.prex=false;
                p.postx=true;
            }/*note that yfy would give rise to ambiguity*/
            else return null;
            return p;
        }
        return null;
    }

    public static void makeops() {
        if(term.emptylist!=null)
            return;
        term.emptylist=term.newconstant("[]","[]");

        AND=",";
        OR=";";
        MATCH="=";
        ARROW=":-";
        CUT="!";
        REWRITE="-->";

        preops=new Hashtable();
        inops=new Hashtable();
        postops=new Hashtable();
        addoperator("?-","fx",1200);//
        addoperator(ARROW,"xfx",1200);//the if
        addoperator(ARROW,"fx",1200);//the do in programs
        addoperator(REWRITE,"xfx",1200);//grammar rules
        addoperator("not","fx",900);
        addoperator(OR,"xfy",1100);//the ;
        addoperator(AND,"xfy",1000);//the ,
        addoperator(MATCH,"xfx",700);//matchable
        addoperator("==","xfx",700);//exactly the same
        addoperator("\\==","xfx",700);//not the same
        addoperator(">","xfx",700);//compare values
        addoperator("<","xfx",700);//compare values
        addoperator(">=","xfx",700);//compare values
        addoperator("<=","xfx",700);//compare values
        addoperator("is","xfx",700);// calculate right
        addoperator("=:=","xfx",700);//values equal
        addoperator("=\\=","xfx",700);//values unequal
        addoperator("=..","xfx",700);//compose a(b)=..[a,b]

        addoperator("+","yfx",500);
        addoperator("-","yfx",500);
        addoperator("-","fx",500);
        addoperator("+","fx",500);
        addoperator("*","yfx",400);
        addoperator("/","yfx",400);
        addoperator("div","yfx",400);
        addoperator("mod","xfx",300);

        listcons=make(".","xfy",600);
    }
    public static boolean addoperator(String s,String type,int level) {
        prologop op=make(s,type,level);
        if(op==null)
            return false;
        if(op.place==op.pre)
            preops.put(s,op);
        else if(op.place==op.in)
            inops.put(s,op);
        else
            postops.put(s,op);
        return true;
    }

    public static prologop preop(String name)
    {return (prologop)preops.get(name);}
    public static prologop inop(String name)
    {return (prologop)inops.get(name);}
    public static prologop postop(String name)
    {return (prologop)postops.get(name);}

    int under(prologop o1,prologop o2) {
        /*1 means that that o1 can be under o2: like 3*4+2
          2 means 2+3*4.
          0 means they cannot be combined. for example let <-- be xfx,
          then a <-- b <-- c is a syntax error.
        */
        if(o1.priority<o2.priority)
            return 1;
        if(o1.priority>o2.priority)
            return 2;
        if(!o2.prex)
            return 1;
        if(!o1.postx)
            return 2;
        return 0;
    }
    int leftunderlevel() {
        if(prex)
            return priority-1;
        return priority;
    }
    int rightunderlevel() {
        if(postx)
            return priority-1;
        return priority;
    }

}

/**********************************************************
 *  P R O G R A M
 **********************************************************/

class program {
    static Hashtable prelude;
    Hashtable user;
    /*the hashtables stores lists of clause of certain name and arity.*/
    solver sv;

    program() {
        if(prelude==null)
            makeprelude();
        user=new Hashtable();
        fillwith(user,prelude);
    }
    void setsolver(solver S) { sv=S; }

    static void makeprelude() {
        prelude=new Hashtable();
        assert(prelude,"member(X,[X|_]).");
        assert(prelude,"member(X,[_|H]):-member(X,H).");
        assert(prelude,"not(X):-X,!,fail.");
        assert(prelude,"not(X).");
        assert(prelude,"append([],B,B).");
        assert(prelude,"append([A|B],C,[A|D]):-append(B,C,D).");
        assert(prelude,"select([X|B],X,B).");
        assert(prelude,"select([A|B],X,[A|C]):-select(B,X,C).");
        assert(prelude,"reverse(X,XR):-reverse(X,[],XR).");
        assert(prelude,"reverse([],XR,XR).");
        assert(prelude,"reverse([H|T],TR,XR):-reverse(T,[H|TR],XR).");
        assert(prelude,"permutation([],[]).");
        assert(prelude,"permutation(LX,[X|LP]):-select(LX,X,L),permutation(L,LP).");
    }
    void remove(String s) {
        /*remove a searchkey.*/
        user.remove(s);
    }

    static void fillwith(Hashtable to,Hashtable from) {
        Enumeration enum=from.elements();
        term oldlist,newlist;
        while(enum.hasMoreElements()) {
            oldlist=(term)enum.nextElement();
            if(oldlist!=term.emptylist)
                to.put(searchkey(head(oldlist.arg[0])),copylist(oldlist));
        }
    }
    static term copylist(term list) {
        if(list==term.emptylist)
            return list;
        return term.makelist(list.arg[0],copylist(list.arg[1]));
    }
    static void listaddz(term list,term t) {
        list=term.skipeq(list);
        while(list.arg[1]!=term.emptylist)
            list=list.arg[1];
        list.arg[1]=term.makelist(t,term.emptylist);
    }

    public String toString() {
        Enumeration enum=user.elements();
        Vector v;
        term t;
        StringBuffer buf=new StringBuffer("\n");
        while(enum.hasMoreElements()) {
            term list=(term)enum.nextElement();
            while(list!=term.emptylist) {
                buf.append(list.arg[0] +".\n");
                list=list.arg[1];
            }
            buf.append("\n");
        }
        return buf.toString();
    }

    static boolean assert(Hashtable h,String s)
    {return assert(h,new prologtokenizer(s).gettermdot(null));}

    static term gramconvert(term t) {
        //we assume no eq's in the term (it is parsed or a copy).
        if(t.type!=term.FUNCTOR||t.arity!=2||!t.name.equals(prologop.REWRITE))
            return t;
        term a,b;
        a=new term();/*differencelist a-b*/
        b=new term();
        t.arg[0].addarg(a);
        t.arg[0].addarg(b);
        t.arg[1]=makediflist(t.arg[1],a,b);
        t.name=prologop.ARROW;
        return t;
    }

    static term makediflist(term t,term a,term b) {
        //t has no eq's
        if(t.type!=term.FUNCTOR)
            return term.newconstant("error");
        if(t.name.equals("[]")&&t.arity==0) {
            term is=term.newconstant(prologop.MATCH);
            is.addarg(a);
            is.addarg(b);
            return is;
        }
        if(t.name.equals(prologop.listcons.name)) {
            listend(t,b);
            term is=term.newconstant(prologop.MATCH);
            is.addarg(a);
            is.addarg(t);
            return is;
        }
        if(t.name.equals(prologop.AND)) {
            term c=new term();
            t.arg[0]=makediflist(t.arg[0],a,c);
            t.arg[1]=makediflist(t.arg[1],c,b);
            return t;
        }
        if(t.name.equals(prologop.OR)) {
            t.arg[0]=makediflist(t.arg[0],a,b);
            t.arg[1]=makediflist(t.arg[1],a,b);
            return t;
        }
        t.addarg(a);
        t.addarg(b);
        return t;
    }

    static void listend(term list,term t) {
        while(list.arg[1]!=term.emptylist)
            list=list.arg[1];
        list.arg[1]=t;
    }

    static boolean assert(Hashtable h,term t) {
        if(t==null)  return false;
        term thead=head(t);
        if(thead==null)  return false;
        term list=(term)h.get(searchkey(thead));
        if(list==null||list==term.emptylist) {
            list=term.makelist(t,term.emptylist);
            h.put(searchkey(thead),list);
        }
        else  listaddz(list,t);
        return true;
    }

    static boolean asserta(Hashtable h,term t) {
        if(t==null)  return false;
        //t=term.skipeq(t);
        term thead=head(t);
        if(thead==null)  return false;
        term list=(term)h.get(searchkey(thead));
        if(list==null)
            list=term.makelist(t,term.emptylist);
        else
            list=term.makelist(t,list);
        h.put(searchkey(thead),list);
        return true;
    }

    static String searchkey(term t)
    /*calculates a key for a head, to find it in a hashtable*/
    {return t.name+"/"+t.arity;}

    term get(term head)
    /*give a predicate, it returns a list of all clauses */
    {return (term)user.get(searchkey(head));}

    static term head(term t) {
        if(t.type!=t.FUNCTOR)               return null;
        if(t.name.equals(prologop.ARROW))   return t.arg[0];
        else return t;
    }
    static term body(term t) {
        if(t.type==t.FUNCTOR&&t.name.equals(prologop.ARROW))
            return t.arg[1];
        return null;
    }
}

/**********************************************************
 *  R A C K
 **********************************************************/

class rack {
    term pred;
    int solveoption;
    static int BUILTIN=-4,NOTAGAIN=-2,UNKNOWN=-1;
    term clauses;
    int substdone;
    rack parent;

    rack(term h,rack p) {
        pred=h;
        solveoption=UNKNOWN;
        substdone=0;
        parent=p;
    }
}

/**********************************************************
 *  S O L V E R
 **********************************************************/
class solver {
    Stack todo;
    Stack done;
    Stack subst;
    Thread mythread;
    program lib;
    Vector uservars;
    prologtokenizer inp;
    Stack consultstack;
    static Hashtable bi_pred;
    long time;
    static term ASK;
    boolean wait;

    void bi(String s,int a,int n)
    {bi_pred.put(s+"/"+a,new Integer(n));}

    solver(program l) {
        if(ASK==null) {
            ASK=term.newconstant("ask user ");
            bi_pred=new Hashtable();
            bi("repeat",0,1);
            bi("fail",0,2);
            bi("true",0,3);
            bi("!",0,4);
            bi("=",2,5);
            bi("is",2,6);
            bi("=:=",2,7);
            bi("<",2,8);
            bi(">",2,9);
            bi("<=",2,10);
            bi(">=",2,11);
            bi("=\\=",2,12);
            bi("get",1,15);
            bi("get0",1,16);
            bi("seen",0,17);
            bi("nl",0,20);
            bi("put",1,21);
            bi("told",0,22);
            bi("newprogram",0,23);
            bi(prologop.listcons.name,2,25);
            bi("assert",1,26);
            bi("assertz",1,26);
            bi("asserta",1,27);
            bi("retract",1,28);
            bi("writeprogram",0,29);
            bi("op",3,30);
            bi("var",1,31);
            bi("nonvar",1,32);
            bi("atom",1,33);
            bi("integer",1,34);
            bi("=..",2,35);
            bi("name",2,36);
            bi("==",2,37);
            bi(";",2,38);
            bi(",",2,39);
            bi("compound",1,40);
            bi("random",3,41);
            bi("\\==",2,42);
            bi("writenoq",1,43);
            bi("writeq",1,44);
        }
        lib=l;
    }
    int getbinum(term r) {
        Integer i=(Integer)bi_pred.get(lib.searchkey(r));
        if(i==null)
            return -1;
        return i.intValue();
    }

    void stacktodo(term q,rack r)
    { /*push all goals in q on the todo stack, with parent r.*/
        if(q==null)
            return;
        if(q.type==q.FUNCTOR&&q.name==prologop.AND) {
            stacktodo(q.arg[1],r);
            stacktodo(q.arg[0],r);
        } else todo.push(new rack(q,r));
    }

    void query(term q) {
        time=System.currentTimeMillis();
        todo=new Stack();
        done=new Stack();
        subst=new Stack();
        todo.push(new rack(ASK,null));
        uservars=new Vector();
        term.vars(q,uservars);
        stacktodo(q,null);
        run(5000); // max iters
        //mythread=new Thread(this);
        //mythread.start();
    }

    static int FALSE=0,TRUE=1,ERROR=-1;
    int solve(rack r) {
        /*return one of FALSE=0,TRUE=1,ERROR=-1*/
        if(r.solveoption==r.NOTAGAIN)
            return FALSE;
        term rpred=term.skipeq(r.pred);
        if(rpred.type!=term.FUNCTOR)
            return ERROR;
        String fname=rpred.name;

        int bi=getbinum(rpred);
        if(bi!=-1) {
            char c;
            term t,l;

            switch(bi){
            case 1:return TRUE;
            case 2:return FALSE;
            case 3:
                r.solveoption=r.NOTAGAIN;
                return TRUE;
            case 4:
                r.solveoption=r.NOTAGAIN;
                rack todo1;
                rack realparent=r.parent;
                while(realparent!=null&&
                      (realparent.pred.name==prologop.AND||
                       realparent.pred.name==prologop.OR))
                    {realparent=realparent.parent;}
                int todop=todo.size()-1;
                while(todop>=0) {
                    todo1=(rack)todo.elementAt(todop);
                    if(todo1.parent!=null&&
                       (todo1.parent.pred.name==prologop.AND||
                        todo1.parent.pred.name==prologop.OR) )
                        {
                            todo1.parent=realparent;
                            todop--;
                        }
                    else break;
                }
                r.parent=realparent;

                while(!done.empty()) {
                    if(done.peek()!=r.parent)
                        done.pop();
                    else
                        break;
                }
                if(r.parent!=null)
                    r.parent.solveoption=r.NOTAGAIN;
                return TRUE;
            case 5:
                r.solveoption=r.NOTAGAIN;
                if(term.match(rpred.arg[0],rpred.arg[1],subst))
                    return TRUE;
                else
                    return FALSE;
            case 6:
                r.solveoption=r.NOTAGAIN;
                int n=term.numbervalue(rpred.arg[1]);
                if(n==term.NaN)
                    return ERROR;
                if(term.match(rpred.arg[0],new term(n),subst))
                    return TRUE;
                else
                    return FALSE;
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                r.solveoption=r.NOTAGAIN;
                int n1=term.numbervalue(rpred.arg[0]);
                int n2=term.numbervalue(rpred.arg[1]);
                if(n1==term.NaN||n2==term.NaN) {
                    System.out.println("No number");
                    return ERROR;
                }
                if((bi==8&&n1<n2)||
                   (bi==9&&n1>n2)||
                   (bi==10&&n1<=n2)||
                   (bi==11&&n1>=n2)||
                   (bi==7&&n1==n2)||
                   (bi==12&&n1!=n2))
                    return TRUE;
                else
                    return FALSE;
            case 15:
                r.solveoption=r.NOTAGAIN;
                do{
                    c=inp.get0();
                }while(c<=32);
                if(term.match(rpred.arg[0],new term((int)c),subst))
                    return TRUE;
                else
                    return FALSE;
            case 16:
                r.solveoption=r.NOTAGAIN;
                c=inp.get0();
                if(term.match(rpred.arg[0],new term((int)c),subst))
                    return TRUE;
                else
                    return FALSE;
            case 17:
                r.solveoption=r.NOTAGAIN;
                inp=null;
                return TRUE;
            case 19:
                r.solveoption=r.NOTAGAIN;
                return TRUE;
            case 20:
                r.solveoption=r.NOTAGAIN;
                return TRUE;
            case 21:
                r.solveoption=r.NOTAGAIN;
                t=term.skipeq(rpred.arg[0]);
                if(t.type==term.NUMBER&&t.arity>=0&&t.arity<256)
                    {
                        return TRUE;
                    }
                return ERROR;
            case 22:
                r.solveoption=r.NOTAGAIN;
                return TRUE;
            case 23:
                r.solveoption=r.NOTAGAIN;
                lib=new program();
                return TRUE;
            case 24:
            case 25:
                r.solveoption=r.NOTAGAIN;
                t=term.skipeq(rpred.arg[0]);
                if(t.type!=term.FUNCTOR||t.arity!=0)
                    return ERROR;
                return TRUE;
            case 26:
                r.solveoption=r.NOTAGAIN;
                if(assert(program.gramconvert(rpred.arg[0].copy())
                          ))
                    return TRUE;
                return ERROR;
            case 27:
                r.solveoption=r.NOTAGAIN;
                if(program.asserta(lib.user,
                                   program.gramconvert(rpred.arg[0].copy())
                                   ))
                    return TRUE;
                return ERROR;
            case 28:
                r.solveoption=r.NOTAGAIN;
                return retract(rpred.arg[0]);
            case 29:
                r.solveoption=r.NOTAGAIN;
                return TRUE;
            case 30:
                r.solveoption=r.NOTAGAIN;
                term nam=term.skipeq(rpred.arg[2]);
                if(nam.type!=term.FUNCTOR||nam.arity!=0)
                    return ERROR;
                term ty=term.skipeq(rpred.arg[1]);
                if(ty.type!=term.FUNCTOR||ty.arity!=0)
                    return ERROR;
                if(prologop.addoperator(nam.name,ty.name,
                                        term.numbervalue(rpred.arg[0])))
                    return TRUE;
                return ERROR;
            case 31:
                r.solveoption=r.NOTAGAIN;
                t=term.skipeq(rpred.arg[0]);
                if(t.type==term.OPEN)
                    return TRUE;
                return FALSE;
            case 32:
                r.solveoption=r.NOTAGAIN;
                t=term.skipeq(rpred.arg[0]);
                if(t.type!=term.OPEN)
                    return TRUE;
                return FALSE;
            case 33:
                r.solveoption=r.NOTAGAIN;
                t=term.skipeq(rpred.arg[0]);
                if(t.type==term.FUNCTOR&&t.arity==0)
                    return TRUE;
                return FALSE;
            case 34:
                r.solveoption=r.NOTAGAIN;
                t=term.skipeq(rpred.arg[0]);
                if(t.type==term.NUMBER)
                    return TRUE;
                return FALSE;
            case 35:
                r.solveoption=r.NOTAGAIN;
                term left=term.skipeq(rpred.arg[0]);
                if(left.type==term.FUNCTOR)
                    {
                        term tail=term.emptylist;
                        for(int i=left.arity-1;i>=0;i--)
                            tail=term.makelist(left.arg[i],tail);
                        term head=term.newconstant(left.name,left.qname);
                        if(term.match(term.makelist(head,tail),rpred.arg[1],subst))
                            return TRUE;
                        return FALSE;
                    }
                term right=term.skipeq(rpred.arg[1]);
                if(right.type==term.FUNCTOR&&right.name==prologop.listcons.name)
                    {
                        term h=term.skipeq(right.arg[0]);
                        if(h.type==term.FUNCTOR&&h.arity==0)
                            {t=term.newconstant(h.name,h.qname);
                            l=term.skipeq(right.arg[1]);
                            while(l!=term.emptylist)
                                {
                                    if(t.arity==term.MAXARG||l.type!=term.FUNCTOR
                                       ||l.name!=prologop.listcons.name)
                                        return ERROR;
                                    t.addarg(term.skipeq(l.arg[0]));
                                    l=term.skipeq(l.arg[1]);
                                }
                            if(term.match(left,t,subst))
                                return TRUE;
                            return FALSE;
                            }
                    }
                return ERROR;
            case 36:
                r.solveoption=r.NOTAGAIN;
                t=term.skipeq(rpred.arg[0]);
                if(t.type==term.FUNCTOR&&t.arity==0)
                    {if(term.match(rpred.arg[1],term.asciilist(t.name),subst))
                        return TRUE;
                    return FALSE;
                    }
                String str=term.readasciilist(rpred.arg[1]);
                if(str!=null&&term.match(rpred.arg[0],term.newconstant(str),subst))
                    return TRUE;
                return FALSE;
            case 37:
                r.solveoption=r.NOTAGAIN;
                if(term.equal(rpred.arg[0],rpred.arg[1]))
                    return TRUE;
                else
                    return FALSE;
            case 38:
                if(r.solveoption==r.UNKNOWN)
                    {r.solveoption=r.BUILTIN;
                    stacktodo(rpred.arg[0],r);
                    return TRUE;
                    }
                r.solveoption=r.NOTAGAIN;
                stacktodo(rpred.arg[1],r);
                return TRUE;
            case 39:
                r.solveoption=r.NOTAGAIN;
                stacktodo(rpred.arg[1],r);
                stacktodo(rpred.arg[0],r);
                return TRUE;
            case 40:
                r.solveoption=r.NOTAGAIN;
                t=term.skipeq(rpred.arg[0]);
                if(t.type==term.FUNCTOR&&t.arity>0)
                    return TRUE;
                return FALSE;
            case 41:
                r.solveoption=r.NOTAGAIN;
                int a=term.numbervalue(rpred.arg[0]);
                int b=term.numbervalue(rpred.arg[1]);
                if(a<=b&&a!=term.NaN&&b!=term.NaN)
                    {
                        if(term.match(rpred.arg[2],
                                      new term(a+(int)(Math.random()*(b-a))),
                                      subst))
                            return TRUE;
                        return FALSE;
                    }
                return ERROR;
            case 42:
                r.solveoption=r.NOTAGAIN;
                if(term.equal(rpred.arg[0],rpred.arg[1]))
                    return FALSE;
                else
                    return TRUE;
            case 43:
                r.solveoption=r.NOTAGAIN;
                return TRUE;
            case 44:
                r.solveoption=r.NOTAGAIN;
                return TRUE;
            default:
                System.out.println("bipred missing.");
                return ERROR;
            }}//OD switch, OD bi!=-1
        if(rpred==ASK)
            {
                if(uservars.size()==0)
                    return TRUE;
                substwrite();
                return FALSE;
            }
        /*No builtin predicate. Get a fitting rule out the
          clause lib, match the head, and stack the body.*/
        if(r.solveoption==r.UNKNOWN) {
            r.clauses=lib.get(rpred);
            if(r.clauses==null) {
                System.out.println("undefined predicate: "+lib.searchkey(rpred));
                return FALSE;
            }
        }
        else if(r.clauses!=term.emptylist)
            r.clauses=r.clauses.arg[1];
        r.solveoption=1;
        term theclause;
        while(r.clauses!=term.emptylist) {
            theclause=r.clauses.arg[0].copy();
            if(term.match(rpred,lib.head(theclause),subst)) {
                stacktodo(lib.body(theclause),r);
                return TRUE;
            }
            r.clauses=r.clauses.arg[1];
        }
        return FALSE;
    }

    public void run(int max_iter) {
        rack current;
        wait=false;
        int substnum;

        int iter = 0;
        while(iter++ < max_iter) {
            if(todo.size()==0) {
                System.out.println("\nyes");
                return;
            }
            current=(rack)todo.pop();
            int v=solve(current);
            if(v==ERROR) {
                do {
                    System.out.println("Error in: "+current.pred);
                    current=current.parent;
                } while(current!=null);
                return;
            }
            else if(v==TRUE) {
                current.substdone=subst.size();
                done.push(current);
            } else {//v==FALSE
                current.solveoption=rack.UNKNOWN;
                todo.push(current);
                if(done.isEmpty())  return;
                current=(rack)done.pop();
                while(((rack)todo.peek()).parent==current)
                    {todo.pop();}
                todo.push(current);
                if(!done.isEmpty())
                    substnum=((rack)done.peek()).substdone;
                else
                    substnum=0;
                term.unmatch(subst,substnum);
            }
        }
    }

    term executepred(term X) {
        /*returns A if X == (:-A), or null.*/
        if(X==null)
            return null;
        X=term.skipeq(X);
        if(X.type==term.FUNCTOR&&X.arity==1&&X.name.equals(":-"))
            return X.arg[0];
        return null;
    }

    boolean assert(term X)
    {  return program.assert(lib.user,X);
    }

    int retract(term t) {
        term list=lib.get(t);
        if(list==null||list==term.emptylist)
            return FALSE;
        if(term.match(t,lib.head(list.arg[0]),subst)) {
            lib.user.put(lib.searchkey(t),list.arg[1]);
            return TRUE;
        }
        term superlist=list;
        list=list.arg[1];
        while(list!=term.emptylist) {
            if(term.match(t,list.arg[0],subst)) {
                superlist.arg[1]=list.arg[1];
                return TRUE;
            }
            superlist=list;
            list=list.arg[1];
        }
        return FALSE;
    }

    void substwrite() {
        term g;
        StringBuffer buf=new StringBuffer("\n");
        Hashtable hash = new Hashtable();
        for(int i=0;i<uservars.size();i++) {
            g=(term)uservars.elementAt(i);
            String rightside=""+g;
            String leftside=g.varname();
            if(!leftside.equals("_")&&!leftside.equals(rightside)) {
                hash.put(leftside, rightside);
            }
        }
        answers.addElement(hash);
    }

    Vector answers = new Vector();
    Vector getAnswers() {
        Vector ret = answers;
        answers = new Vector(); // clear for next time
        return ret;
    }
}
