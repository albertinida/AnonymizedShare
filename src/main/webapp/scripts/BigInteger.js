


function abs(a) {
	if(a.charAt(0)=='-')
		return a.substring(1);
	else
		return a;
}

function compareTo(a,b) {
	if(a.length<b.length)
		return -1;
	else 
		if(a.length>b.length)
			return 1;
		else
			if(a.length==b.length){
				for(i=0;i<a.length;i++)
					if(parseInt(s.charAt(i))<parseInt(t.charAt(i)))
						return -1;
					else
						if(parseInt(s.charAt(i))>parseInt(t.charAt(i)))
							return 1;
			}
	return 0;
	
}

function sum(a,b) {
	a = a.toString();
	b = b.toString();
	var sign = "";
	if(a.charAt(0)=='-' && b.charAt(0)=='-') {
		a = a.substring(1);
		b = b.substring(1);
		sign = "-";
	}
	else
		if(a.charAt(0)=='-') {
			a = a.substring(1);
			return subtraction(b,a);
		}
		else
			if(b.charAt(0)=='-'){
				b = b.substring(1);
				return subtraction(a,b);
			}
	var c_in = 0; //riporto in ingresso
	var c_out = 0; // riporto in uscita
	var sum = ""; //somma inizializzata a 0
	var x,y;
	/*
	 * verifica della lunghezza delle stringhe in modo che la più breve sia successiva
	 */
		if(a.length>=b.length) {
			x = a;
			y = b;
		}
		else {
			x = b;
			y = a;
		}
	//aggiunge caratteri "0" davanti alla più corta fino a raggiungere la lunghezza della stringa più lunga
	while(y.length<x.length)
		y = "0"+y;
	
	var num_1, num_2, s;
	for(i=x.length-1;i>=0;i--) {
		// al riporto in ingresso viene assegnato il riporto in uscita della fase precedente
		c_in = c_out;
		//estrazione dell' i-esimo carattere da tutte e due le stringhe
		num_1 = parseInt(x.charAt(i));
		num_2 = parseInt(y.charAt(i)); 
		s = num_1 + num_2 + c_in;	//somma
		if(s>=10) {					//se la somma è maggiore o uguale a 10 
			if(i==0)				//se ci si trova all'ultima iterazione tutto il risultato viene concatenato alla somma
				sum = s+sum;
			else {
				var t = s+"";		// altrimenti c_out viene assegnato il primo carattere e alla somma viene concatenato il secondo
				c_out = parseInt(t.charAt(0));
				sum = t.charAt(1) + sum;
			}
		}
		else {			//se la somma tra due numeri è un valore compreso tra 0 e 9 si procede normalmente
			c_out = 0;
			sum = s+sum;
		}
	}
	return sign+sum;
}


function product(a,b) {
	a = a.toString();
	b = b.toString();
	var sign = "";
	if(a.charAt(0)=='-' && b.charAt(0)=='-'){
		a = a.substrin(1);
		b = b.substring(1);
	}else
		if(a.charAt(0)=='-' && b.charAt(0)!='-') {
			a = a.substring(1);
			sign = "-";
		}else
			if(a.charAt(0)!='-' && b.charAt(0)=='-') {
				b = b.substring(1);
				sign = "-";
			}
	temp = new Array()  //array in cui memorizzare i risultati temporanei
	for(i=temp.length-1;i>=0;i--)
		if(i==temp.length-1)
			temp[i] = "";
		else
			temp[i] = temp[i+1]+"0";
	
	var num1,num2;
	var c_in = 0; //riporto in ingresso
	var c_out = 0; //riporto in uscita
	var p;
	
	for(i=b.length-1;i>=0;i--) {
		c_out = 0; //all'inizio di una nuova iterazione del ciclo esterno il riporto viene azzerato
		num1 = parseInt(b.charAt(i)); //estrazione dell'i-esimo carattere
		for(j=a.length-1;j>=0;j--) {
			c_in = c_out;
			num2 = parseInt(a.charAt(j));  //estrazione del carattere j-esimo dalla stringa
			p = num1*num2 + c_in;
			if(p>=10) {
				if(j==0)    
					temp[i] = p+temp[i];
				else{
					var s = p+"";
					c_out = parseInt(s.charAt(0));
					temp[i] = s.charAt(1) + temp[i];
				}
			}
			else {
				temp[i] = p+temp[i];
				c_out = 0;
			}
		}
	}
	var product = "";
	for(i=0;i<temp.length;i++)
		product = sum(product,temp[i]);
	return sign+product;
	}

function subtraction(a,b) {
	a = a.toString();
	b = b.toString();
	var sign = "";
	if(compareTo(a, b)==-1) {
		sign = "-";
		String temp;
		temp = a;
		a = b;
		b = temp;
	}
	while(b.length<a.length)
		b = "0"+b;
	var result = "";
	var num1,num2;
	for(i=a.length-1;i>=0;i--) {
		num1 = parseInt(a.charAt(i));
		num2 = parseInt(b.charAt(i));
		if(num1<num2) {
			var j = parseInt(i-1);
			while(parseInt(a.charAt(j))==0) {
				a = a.substring(0,j)+"9"+a.substring(j+1,a.length);
				j--;
			}
			var n = parseInt(s.charAt(j));
			n = n-1;
			a = a.substring(0,j)+n+a.substring(j+1,a.length);
			num1 = num1 + 10;
		}
		var diff = num1 - num2;
		result = diff+result;
	}
	return result;
}
































