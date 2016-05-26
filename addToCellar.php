<?PHP
	ini_set('display_errors', '1');
	error_reporting(E_ALL);
	$email = $_GET['email'];
	$command = $_GET['cmd'];
	$choice = $_GET['choice'];
	$brand = $_GET['brand'];
	$title = $_GET['title'];
	$year = $_GET['year'];
	$type = $_GET['type'];


	// Connect to the Database
	$dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=_450btm3';
    $username = '_450btm3';
    $password = 'Ruxyut';

    //?email=yyang3@uw.edu&cmd=Cellar&choice=add&brand=Dogfish%20Head&title=90%20Minute%20IPA&year=2016&type=beer

    try {
    	$db = new PDO($dsn, $username, $password);

    	if ($command == "Cellar") {
			$select_sql = '';

			if ($choice == "add") {

				$stmt = $db->prepare("SELECT id FROM Beverage WHERE brand = '$brand' AND title = '$title' AND Brewyear = '$year'");
    			$stmt->execute();

    			// set the resulting array to associative
    			$id = $stmt->fetchAll(PDO::FETCH_ASSOC);

    			$id = $id[0]["id"];


        	}
        }
    } catch (PDOException $e) {
    	$error_message = $e->getMessage();
        echo 'There was an error connecting to the database.';
		echo $error_message;
        exit();
    }
?>