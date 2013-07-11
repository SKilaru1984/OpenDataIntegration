This is the Java API to create and publish datasets to the Socrata portal using the Socrata Open Data API (SODA).

1) To create the dataset we will need the dataset list and the data dictionary files.
	
	a) The dataset list contains the metadata required for the dataset.
	b) The data dictionary contains  the column names and their description.
	c) The dataset list file and the data dictionary file should be placed in the same folder.
	Note: Dataset list and data dictionary should be in excel format, the sample files can be found in the src/test/resources folder.

2) The JDK should be 1.6 or greater.

3) Prerequisites: For this program to run successfully 

	a) The database tables and procedures have to be created and the database connection details have
		to be updated in the properties file which can be found under src/main/resources folder
		
		Note: The database scripts can be found in the src/test/resources folder. 
			These scripts support Oracle 10g database, for other database's make changes accordingly.
	
	b) Provide the encrypter key in the properties file
		Note: Make sure all the passwords (DB, Socrata) are encrypted using this key.
		(Use gov.nyc.opendata.integration.main.StringEncrypterUtil.java class for encryption)		
			
	c) If proxy exists please provide the proxy details and set proxy.enabled to true.
		
		Note: This program will be using an unauthenticated connection through the proxy.
		 
	d) Update the mail configuration in the properties file.
	
	e) Socrata credential's should be updated in the NYCOpenDataIntegration.properties file.
		Note: Also make sure the log location is provided in log4j.properties
	
	f) Apache Maven should be configured.
		Note: ojdb-14.jar has some issue through maven. If any issue is faced try to download the jar 
			  and place it manually in repository.
	

4) Step by step process
	
	a) Once the program is triggered it will look up the control table to check for any datasets with the status ‘Generated’, 
		if any datasets are available then the following  values for the dataset are retrieved from the database
		
			i.   Dataset_Id : Id of the dataset created in the Socrata portal(if the dataset is not created this valus will be null)
			ii.  DataSet_Name: Name of the dataset.
			iii. Flag: Upsert-U or Append-A or Replace-R
			iv.  DataFile_Path: Path to the data file (Sample data file can be found in src/test/resources folder ).
			v.   DataDictionary_path: Path to the dataset list file.
			vi.  Agency_Contact: Email address to send notification in case of failure 
	
	b) The polling duration is set based on the value set in the properties file.
	
	c)	Java program flips the flag to ‘JavaStart ’in the database for the  data set listed in step a and start publishing the data set in a sequence 
	
	d)	Depending on the number of datasets to be published the size of the Blocking Queue (java.util.concurrent.BlockingQueue) is set accordingly.
	
	e)	The datasets are then processed in batches of three using the ThreadPoolExecutor (java.util.concurrent.ThreadPoolExecutor) whose core and max pool size are set to three (So three threads can run concurrently).
	
	f)  If the dataset id is not null then the steps  (h) to (k) are followed.
	
	g)  If the dataset id is null then the data set file and data dictionary file are read to get the necessary details and the dataset is created
		(Make sure the data set file and data dictionary file are in the same directory and the location of the dataset list file should be provided in the DB in the DataDictionary_path field) 
	
	h)	The Flag associated with each dataset is checked and depending on the value of the flag one the following operations is performed
		i.	 Upsert
		ii.  Append
		iii. Replace
	
	i)	Once the data set is published successfully  program should flip the flag to ‘Published’ in the staging table for the respective dataset and the ready to publish data file will be purged from the directory and 
		a success notification will be sent to the support team
	
	j)	In case of any failure during the data set publishing process the Java program will flip the flag to ‘JavaFail’ for the dataset and will send a notification to the support team and the agency contact.
	
	k)	After all the datasets are processed, the java component will query the database to check if any datasets are still pending to be processed. If any datasets are available then the steps (a) through (j) are repeated.
		This process continues every 15 mins till the end of polling duration.
	
