<?PHP
ini_set('display_errors', '1');
error_reporting(E_ALL);
$command = $_GET['cmd'];
$email = $_GET['email'];
$type = $_GET['type'];

	// Connect to the Database
		$dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=_450btm3';
        $username = '_450btm3';
        $password = 'Ruxyut';

    	try {
        	$db = new PDO($dsn, $username, $password);
	        	
			if ($command == "Cellar") {
				$select_sql = '';


				if ($type == "All") {
					$select_sql = 'SELECT Cellar.email, Cellar.imageadd, Cellar.description, Cellar.rate,
					Beverage.* FROM Cellar, Beverage WHERE Cellar.id = Beverage.id';
					$select_sql .= " AND email = '" . $email . "'";	
				} else {
					$select_sql = 'SELECT Cellar.email, Cellar.imageadd, Cellar.description, Cellar.rate,
					Beverage.* FROM Cellar, Beverage WHERE Cellar.id = Beverage.id';
					$select_sql .= " AND email = '" . $email . "'";
					$select_sql .= " AND btype = '" . $type . "'";
				}
				
				$Cellar_query = $db->query($select_sql);
				$Cellar = $Cellar_query->fetchAll(PDO::FETCH_ASSOC);
				if ($Cellar) {	
	   				echo json_encode($Cellar);
				}
			}

    	} catch (PDOException $e) {
        	$error_message = $e->getMessage();
        	echo 'There was an error connecting to the database.';
			echo $error_message;
        	exit();
    	}

?>
