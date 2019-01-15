@batch-collect-results
Feature: Verify print a batch results processing

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * eval db.runScript('batch-acceptance-test-data.sql')
    * def S3Utils = Java.type('uk.gov.service.bluebadge.test.utils.S3Utils')
    * def s3 = new S3Utils()
    * def System = Java.type('java.lang.System')
    * def env = System.getenv('bb_env')
    * def inBucketName = 'uk-gov-dft-' + (env == null ? 'ci' : env) +'-badge-in'
    * def result = callonce read('./oauth2-print-batch.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify end point and process pending batches
    Given path 'badges/collect-batches'
    When method GET
    Then status 200

  Scenario: Verify batch results
    * eval db.runScript('batch-acceptance-test-data.sql')
    * eval s3.putObject(inBucketName, '/processedbatchxmlfiles/ValidConfirmation2Badges.xml', 'ValidConfirmation2Badges.xml')
    * eval s3.putObject(inBucketName, '/processedbatchxmlfiles/ValidRejection2Badges.xml', 'ValidRejection2Badges.xml')
    * eval s3.putObject(inBucketName, '/processedbatchxmlfiles/ConfirmationBadgeNotExist (2).xml', 'ConfirmationBadgeNotExist (2).xml')
    * assert 'PROCESSED' == db.getBadgeStatus('UNC001')
    * assert 'PROCESSED' == db.getBadgeStatus('REJ001')
    * assert 'PROCESSED' == db.getBadgeStatus('REJ002')
    * assert 'PROCESSED' == db.getBadgeStatus('CONF02')
    * assert 'PROCESSED' == db.getBadgeStatus('CONF02')
    * assert s3.objectExists(inBucketName, 'ValidConfirmation2Badges.xml')
    * assert s3.objectExists(inBucketName, 'ValidRejection2Badges.xml')
    * assert s3.objectExists(inBucketName, 'ConfirmationBadgeNotExist (2).xml')
    * def batchCountBefore = db.countBatches()
    Given path 'badges/collect-batches'
    When method GET
    Then status 200
  # And a batch is created for each result file
    * assert batchCountBefore + 3 == db.countBatches()
  # And badge statuses updated where badge existed
    * assert 'PROCESSED' == db.getBadgeStatus('UNC001')
    * assert 'REJECT' == db.getBadgeStatus('REJ001')
    * assert 'REJECT' == db.getBadgeStatus('REJ002')
    * assert 'ISSUED' == db.getBadgeStatus('CONF02')
    * assert 'ISSUED' == db.getBadgeStatus('CONF02')
  # And the batch result files deleted
    * assert !s3.objectExists(inBucketName, 'ValidConfirmation2Badges.xml')
    * assert !s3.objectExists(inBucketName, 'ValidRejection2Badges.xml')
    # Check a file with space in name too.
    * assert !s3.objectExists(inBucketName, 'ConfirmationBadgeNotExist (2).xml')

