
  CREATE TABLE "OPR_DATASET_CONTROL" 
   ("DATASET_ID" VARCHAR2(25 BYTE), 
	"DATASET_NM" VARCHAR2(100 BYTE), 
	"AGCY_NM" VARCHAR2(50 BYTE), 
	"DATASET_FILE_NM" VARCHAR2(255 BYTE), 
	"DATASET_UPDT_TYP_NM" VARCHAR2(50 BYTE), 
	"DATASET_PROC_STS_NM" VARCHAR2(50 BYTE), 
	"DATASET_CTCT_EMAIL_ID" VARCHAR2(255 BYTE), 
	"ROW_CRE_TMSTMP" TIMESTAMP (6), 
	"ROW_UPDT_TMSTMP" TIMESTAMP (6), 
	"DATASET_DICTIONARY_NM" VARCHAR2(255 BYTE)
   ) ;

Insert into OPR_DATASET_CONTROL (DATASET_ID,DATASET_NM,AGCY_NM,DATASET_FILE_NM,DATASET_UPDT_TYP_NM,DATASET_PROC_STS_NM,DATASET_CTCT_EMAIL_ID,ROW_CRE_TMSTMP,ROW_UPDT_TMSTMP,DATASET_DICTIONARY_NM) values ('TEST_ID','Test_DataSet','Test_Agency','TestFilePath','C','PUBLISHED','support_contact@doitt.nyc.gov',to_timestamp('29-JAN-13 02.48.28.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('13-MAY-13 04.57.38.726295000 PM','DD-MON-RR HH.MI.SS.FF AM'),'Test_Path');

  
commit;

--------------------------------------------------------
--  File created - Wednesday-May-15-2013   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Package NYC_OPEN_DATA_PACK
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "NYC_OPEN_DATA_PACK" 
AS 
PROCEDURE UPDATE_PUBLISH_STATUS(
   data_set_name_val IN   VARCHAR2,
   flag_val          IN   CHAR,
   ups_return        OUT  VARCHAR2
);
 
PROCEDURE GET_GENERATED_DATASETS(
   O_DATA_SETS OUT SYS_REFCURSOR
);
PROCEDURE GET_DATASET_PATH(
   data_set_name_val IN VARCHAR2,
   flag_val IN CHAR,
   data_setpath OUT VARCHAR2
 );


PROCEDURE UPDATE_DATASET_ID(
   data_set_name_val IN   VARCHAR2,
   data_set_id       IN   VARCHAR2,
   flag_val          IN   CHAR,
   data_SetPath      OUT  VARCHAR2
);
END NYC_OPEN_DATA_PACK ;

/

--------------------------------------------------------
--  File created - Wednesday-May-15-2013   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Package Body NYC_OPEN_DATA_PACK
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "NYC_OPEN_DATA_PACK" AS 
  PROCEDURE update_publish_status(
  data_Set_Name_val IN VARCHAR2,
   flag_val IN CHAR,
   ups_return OUT VARCHAR2
   ) IS 
  BEGIN
        UPDATE OPR_DATASET_CONTROL SET DATASET_PROC_STS_NM = flag_val,row_updt_tmstmp = SYSTIMESTAMP  where DATASET_NM = data_set_name_val;
        COMMIT;
        ups_return := 'Success';

       EXCEPTION
        WHEN OTHERS THEN
        ROLLBACK;
   END update_publish_status;
   
   PROCEDURE get_generated_datasets(
     o_data_sets OUT SYS_REFCURSOR
    )IS
     BEGIN
      OPEN o_data_sets FOR
       Select DATASET_NM as datasetname,DATASET_ID as datasetid,DATASET_FILE_NM as datafile,DATASET_UPDT_TYP_NM as flag,DATASET_CTCT_EMAIL_ID as agencyContact,DATASET_DICTIONARY_NM as dataDictionary from OPR_DATASET_CONTROL where DATASET_PROC_STS_NM = 'GENERATED';
     
      UPDATE OPR_DATASET_CONTROL SET DATASET_PROC_STS_NM ='JAVASTART' where DATASET_ID in (Select DATASET_ID from OPR_DATASET_CONTROL where DATASET_PROC_STS_NM = 'GENERATED');
      COMMIT;
     
      EXCEPTION
       WHEN OTHERS THEN
        ROLLBACK;
    END get_generated_datasets;
    
  PROCEDURE get_dataset_Path(
   data_Set_Name_val IN VARCHAR2,
   flag_val IN CHAR,
   data_SetPath OUT VARCHAR2
   ) IS 
  BEGIN
        UPDATE OPR_DATASET_CONTROL SET DATASET_PROC_STS_NM = flag_val where DATASET_NM = data_set_name_val;
        COMMIT;
         Select DATASET_FILE_NM into data_SetPath from OPR_DATASET_CONTROL where DATASET_NM = data_set_name_val and DATASET_PROC_STS_NM = 'PUBLISHED';
        EXCEPTION
        WHEN OTHERS THEN
         ROLLBACK;
   END get_dataset_Path;

PROCEDURE UPDATE_DATASET_ID(
   data_Set_Name_val IN VARCHAR2,
   data_set_id IN VARCHAR2,
   flag_val IN CHAR,
   data_SetPath OUT VARCHAR2
   ) IS 
  BEGIN
        UPDATE OPR_DATASET_CONTROL SET DATASET_ID = data_set_id,DATASET_PROC_STS_NM = flag_val, ROW_UPDT_TMSTMP= SYSTIMESTAMP
        where DATASET_NM = data_set_name_val;
        COMMIT;
         Select DATASET_FILE_NM into data_SetPath from OPR_DATASET_CONTROL where DATASET_NM = data_set_name_val ;
        EXCEPTION
        WHEN OTHERS THEN
         ROLLBACK;

       
   END UPDATE_DATASET_ID;

END NYC_OPEN_DATA_PACK;

/

