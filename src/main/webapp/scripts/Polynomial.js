

function degree(a) {
	var n = parseInt(a.length());
	return n - 1;
}

/*function getCoefficients(a) {
	return this.coefficients;
}
*/
function getCoefficient(a,degree) {
	return a[degree(a) - parseInt(degree)];
}

function setCoefficient(a,coefficient, degree) {
	a[degree(a)-parseInt(degree)] = coefficient.toString();
}

function multiply(a,b) {
	c = new Array();
	var m = a.degree();
	var n = b.degree();
	var w = parseInt(m)+parseInt(n);
	for (i=0; i<=w; i++) 
		c.push("");
	
	var result = c;
	for (i=0; i<=m; i++)
		for (j=0; j<=n; j++) {
			var oldCoefficient = getCoefficient(result,(parseInt(i)+parseInt(j))).toString();
			var addValue = getCoefficient(a,i).multiply(getCoefficient(b,j)).toString();
			setCoefficient(result,sum(oldCoefficient,addValue), parseInt(i)+parseInt(j));
			
		}
	var coefficients = result;
	return coefficients;
}


function evaluate(a,value) {
	var evaluation = "";
	for (i=0; i<=this.degree(); i++) {
		var coefficient = getCoefficient(a,parseInt(i)).toString();
		var pow = "1";
		for(j=0;j<i;j++) // pow(i)
			pow = product(pow,value.toString());
		var t = product(coefficient,pow);
		evaluation =  sum(evaluation , t); //evaluation.add(coefficient.multiply(value.pow(i)));
	}
	return evaluation;
}

function convMultiply(a,b) {
	if(parseInt(getCoefficient(a,degree(a)))!=1 || parseInt(getCoefficient(b,degree(b)))!=1)
		throw "Error: one or more polynomial not in the right form";
	coefficients = new Array();
	var m = degree(a);
	var n = degree(b);
	var w = parseInt(m) + parseInt(m);
	for (i=0; i<=w; i++) 
		coefficients.push("");
	
	for (i=0; i<=w; i++) {
		for (j=Math.max(0, i-n); j<=Math.min(i, m); j++) {
			var x = a[j].toString();
			var y = b[i-j].toString();
			var c = product(x,y);
			var d = coefficients[i].toString();
			setCoefficient(coefficients,sum(d,c),i);
		}
	}
	return coefficients;
}


function toString(a) {
	var output = "";
	for(i=0;i<this.coefficients.length();i++) {
		output = output + a[i].toString()+" ";
	}
	return "["+output+"]";
}

