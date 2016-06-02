<?PHP
	ini_set('display_errors', '1');
	error_reporting(E_ALL);
	$email = $_GET['email'];
	$command = $_GET['cmd'];
	$choice = $_GET['choice'];
	$brand = $_GET['brand'];
	$title = $_GET['title'];
	$style = $_GET['style'];
	$year = $_GET['year'];
	$percentage = $_GET['percentage'];
	$type = $_GET['type'];
	$description = $_GET['description'];
	$location = $_GET['location'];

	// addStuff.php?email=yyang3@uw.edu&cmd=Cellar&choice=add&brand=Silver%20City&title=Ziggy%20Zoggy&style=summer%20ale&year=2016&percentage=5&type=beer&description=Awesome Beer&location=USA
    // Connect to the Database
	$dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=_450btm3';
    $username = '_450btm3';
    $password = 'Ruxyut';

    try {
    	$db = new PDO($dsn, $username, $password);

		if ($command == "Cellar") {
			$select_sql = '';

			if ($choice == "add") {

				$id = getID($db, $brand, $title, $year);

				if ($id < 0) {
					$select_sql = "INSERT INTO Beverage";
					$select_sql.= " VALUES(default,'$brand', '$title', '$location', default,";
					$select_sql.= "	'$year', '$percentage', '$type', '$description', '$style')";
					if ($db->query($select_sql)) {

	                	echo '{"result": "success adding to beverage table"}';
	                } else {
	                	echo '{"result": "fail", "error": "Failed to add beverage"}';
	                }

	                $id = getID($db, $brand, $title, $year);
				}

				// insert beverage into Cellar
				$select_sql = "INSERT INTO Cellar";
	    		$select_sql .= " VALUES('$email', '$id', default, default, default)";

	    		if ($db->query($select_sql)) {
		            echo '{"result": "success adding to users Cellar"}';
		            $db = null;
	            } else {
	                echo '{"result": "fail", "error": "That Beverage already exists in Cellar."}';
	           	}
	        }
	    }
	} catch (PDOException $e) {
    	$error_message = $e->getMessage();
        echo 'There was an error connecting to the database.';
		echo $error_message;
        exit();
    }

    function getID($db, $brand, $title, $year) {
    	$stmt = $db->prepare("SELECT id FROM Beverage WHERE brand = '$brand' AND title = '$title' AND Brewyear = '$year'");
	    $stmt->execute();
	    $id = $stmt->fetchAll(PDO::FETCH_ASSOC);
	    $id = $id[0]["id"];
	    if ($id > 0) {
	    	return $id;
	    } else {
	    	return '-1';
	    }
    }

    // function checkCellarForBeverage($db, $email, $id) {
    // 	echo 'in checkCellarForBeverage';
    // 	$stmt = $db->prepare("SELECT id FROM Cellar WHERE id = '$id' AND email = '$email'");
    // 	$stmt->execute();
	   //  $beverage = $stmt->fetchAll(PDO::FETCH_ASSOC);
	   //  $beverage = $beverage[0]["id"];
	   //  if ($beverage > 0) {
	   //  	return $beverage;
	   //  } else {
	   //  	return1 '-1';
	   //  }
    // }
?>