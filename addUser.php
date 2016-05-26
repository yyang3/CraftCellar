<?PHP
ini_set('display_errors', '1');
error_reporting(E_ALL);

	    // Connect to the Database
	    $dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=_450btm3';
        $username = '_450btm3';
        $password = 'Ruxyut';
       
    	try {
        	$db = new PDO($dsn, $username, $password);
            $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            //get input for email and password
            $username = isset($_GET['username']) ? $_GET['username'] : '';
            $email = isset($_GET['email']) ? $_GET['email'] : '';
            $pwd = isset($_GET['pwd']) ? $_GET['pwd'] : '';

            //validate input
            if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
                echo '{"result": "fail", "error": "Please enter a valid email."}';
            } else if (strlen($pwd) < 6) {
                echo '{"result": "fail", "error": "Please enter a valid password (longer than five characters)."}';
            } else if (strlen($username) < 4) {
                echo  '{"result": "fail", "error": "Please enter a username of length 4 or greater."}';
            } else {   

                //build query
                $sql = "INSERT INTO CellarUser";
                $sql .= " VALUES ('$email', '$pwd', '$username')";
             
                //attempts to add record
                if ($db->query($sql)) {
                    echo '{"result": "success"}';
                    $db = null;
                } 
            }   
        } catch(PDOException $e) {
                if ((int)($e->getCode()) == 23000) {
                    echo '{"result": "fail", "error": "That email address has already been registered."}';
                } else {
                    echo 'Error Number: ' . $e->getCode() . '<br>';
                    echo '{"result": "fail", "error": "Unknown error (' . (((int)($e->getCode()) + 123) * 2) .')"}';
                }
        }
?>
