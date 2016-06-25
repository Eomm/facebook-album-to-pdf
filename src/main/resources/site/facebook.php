<?php

$clientId = "CUT";
$clientSecret = "CUT";
$redirect = 'CUT';
$code = $_GET["code"];

$json = "{}";
if($code!=null){
	$url = 'https://graph.facebook.com/v2.5/oauth/access_token?client_id='.$clientId.
		'&redirect_uri='.$redirect.
		'&client_secret='.$clientSecret.
		'&code='.$code;
		
		
	$opts = array('http' =>
		array(
			'ignore_errors' => true
		)
	);
	$context = stream_context_create($opts);
	$json = file_get_contents($url, false, $context);
	//echo $json;
	//$jsonToken = json_decode($json);
	//echo $jsonToken->{'access_token'};
}
?>
<html>
<head>
	<title>FaceFile Token Viewer</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"/>
	<script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	<meta name="robots" content="nofollow" />
	<meta name="robots" content="noindex">
	<meta name="googlebot" content="noindex">
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	
	<style>
	* {
    -webkit-box-sizing: border-box;
	   -moz-box-sizing: border-box;
	        box-sizing: border-box;
	outline: none;
}

    .form-control {
	  position: relative;
	  font-size: 16px;
	  height: auto;
	  padding: 10px;
		@include box-sizing(border-box);

		&:focus {
		  z-index: 2;
		}
	}

body {
	background: url(http://i.imgur.com/GHr12sH.jpg) no-repeat center center fixed;
    -webkit-background-size: cover;
    -moz-background-size: cover;
    -o-background-size: cover;
    background-size: cover;
}

.login-form {
	margin-top: 60px;
}

form {
	color: #5d5d5d;
	background: #f2f2f2;
	padding: 26px;
	border-radius: 10px;
	-moz-border-radius: 10px;
	-webkit-border-radius: 10px;
}
	form img {
		display: block;
		margin: 0 auto;
		margin-bottom: 35px;
	}
	form input,
	form button {
		font-size: 18px;
		margin: 16px 0;
	}
	form > div {
		text-align: center;
	}
	
.form-links {
	text-align: center;
	margin-top: 1em;
	margin-bottom: 50px;
}
	.form-links a {
		color: #fff;
	}
	</style>
	
	<script>
	var facebookResponse = <?php echo $json; ?>;
	$(function(){
		console.log(facebookResponse);
		
		// error.code // error.message
		$('#token')
			.val(facebookResponse.access_token)
			.click(function () {
				this.select();
			});
			
			
		$('#copy')
			.click(function() {
				var input = document.querySelector('#token');
				input.setSelectionRange(0, input.value.length + 1);
				try {
				  var success = document.execCommand('copy');
				  if (success) {
					$('#copy').trigger('copied', ['Copied!']);
				  } else {
					$('#copy').trigger('copied', ['Copy with Ctrl-c']);
				  }
				} catch (err) {
				  $('#copy').trigger('copied', ['Copy with Ctrl-c']);
				}
			})
			.bind('copied', function(event, message) {
				$(this).attr('value', message)
					.attr('title', "Copy to Clipboard");
			});
	});
	</script>
</head>
<body>
    <div class="container">
		<div class="row" id="pwd-container">
			<div class="col-md-4"></div>

			<div class="col-md-4">
				<section class="login-form">
					<form>
						<img src="logo-site.png" class="img-responsive" alt="" />
						
						<input type="test" id="token" placeholder="Access-Token" class="form-control input-lg" readonly="readonly"/>

						<button type="button" id="copy" class="btn btn-lg btn-primary btn-block">Copy</button>
						
						<div>Copy the access token on the Facebook Gui App</div>
					</form>

					<div class="form-links">
						<a href="#">Create by Eomm</a>
					</div>
				</section>  
			</div>

			<div class="col-md-4"></div>
		</div>

    </div>    
</body>
</html>