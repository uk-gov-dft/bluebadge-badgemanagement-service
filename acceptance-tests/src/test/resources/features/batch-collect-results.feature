@batch-collect-results
Feature: Verify print a batch results processing

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('batch-acceptance-test-data.sql')
    * def S3Utils = Java.type('uk.gov.service.bluebadge.test.utils.S3Utils')
    * def s3 = new S3Utils()
    * def inBucketName = 'uk-gov-dft-' + (env == null ? 'ci' : env) +'-badge-in'
    * def result = callonce read('./oauth2-print-batch.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify end point and process pending batches
    Given path 'badges/collect-batches'
    When method GET
    Then status 200

  Scenario: Verify batch results
    * eval s3.putObject(inBucketName, '/processedbatchxmlfiles/ValidConfirmation2Badges.xml', 'ValidConfirmation2Badges.xml')
    * eval s3.putObject(inBucketName, '/processedbatchxmlfiles/ValidRejection2Badges.xml', 'ValidRejection2Badges.xml')
    * eval s3.putObject(inBucketName, '/processedbatchxmlfiles/ConfirmationBadgeNotExist.xml', 'ConfirmationBadgeNotExist.xml')
    * assert 'PROCESSED' == db.getBadgeStatus('UNC001')
    * assert 'PROCESSED' == db.getBadgeStatus('REJ001')
    * assert 'PROCESSED' == db.getBadgeStatus('REJ002')
    * assert 'PROCESSED' == db.getBadgeStatus('CONF02')
    * assert 'PROCESSED' == db.getBadgeStatus('CONF02')
    * assert s3.objectExists(inBucketName, 'ValidConfirmation2Badges.xml')
    * assert s3.objectExists(inBucketName, 'ValidRejection2Badges.xml')
    * assert s3.objectExists(inBucketName, 'ConfirmationBadgeNotExist.xml')
    * def batchCountBefore = db.countBatches()
    Given path 'badges/collect-batches'
    When method GET
    Then status 200
  # And a batch is created for each result file
    * assert batchCountBefore + 3 == db.countBatches()
  # And badge statuses updated where badge existed
    * assert 'PROCESSED' == db.getBadgeStatus('UNC001')
    * assert 'REJECTED' == db.getBadgeStatus('REJ001')
    * assert 'REJECTED' == db.getBadgeStatus('REJ002')
    * assert 'ISSUED' == db.getBadgeStatus('CONF02')
    * assert 'ISSUED' == db.getBadgeStatus('CONF02')
  # And the batch result files deleted
    * assert !s3.objectExists(inBucketName, 'ValidConfirmation2Badges.xml')
    * assert !s3.objectExists(inBucketName, 'ValidRejection2Badges.xml')
    * assert !s3.objectExists(inBucketName, 'ConfirmationBadgeNotExist.xml')

