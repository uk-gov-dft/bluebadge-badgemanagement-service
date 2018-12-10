--
--    Copyright 2010-2016 the original author or authors.
--
--    Licensed under the Apache License, Version 2.0 (the "License");
--    you may not use this file except in compliance with the License.
--    You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--    Unless required by applicable law or agreed to in writing, software
--    distributed under the License is distributed on an "AS IS" BASIS,
--    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--    See the License for the specific language governing permissions and
--    limitations under the License.
--

-- // BBB-1008-batch_up_badge_records_for_printing
-- Migration SQL that makes the change goes here.
-- Badge table
ALTER TABLE badgemanagement.badge ADD badge_status_timestamp timestamp without time zone DEFAULT null NULL;

-- Batch table
CREATE TABLE badgemanagement.batch
(
  batch_id serial NOT NULL,
  batch_filename character varying(100) NOT NULL,
  batch_created_timestamp timestamp without time zone NOT NULL,
  batch_source character varying(10) NOT NULL,
  batch_purpose character varying(10) NOT NULL,
  CONSTRAINT batch_pk PRIMARY KEY (batch_id)
);

-- Batch_badge table
CREATE TABLE badgemanagement.batch_badge
(
  batch_id integer NOT NULL,
  badge_no character varying(6) NOT NULL,
  CONSTRAINT batch_badge_pkey PRIMARY KEY (batch_id, badge_no)
);

ALTER TABLE badgemanagement.batch_badge
  ADD CONSTRAINT batch_badge_batch_id_fk
    FOREIGN KEY (batch_id)
      REFERENCES badgemanagement.batch(batch_id);

ALTER TABLE badgemanagement.batch_badge
  ADD CONSTRAINT batch_badge_badge_no_fk
    FOREIGN KEY (badge_no)
      REFERENCES badgemanagement.badge(badge_no);

-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE badgemanagement.badge DROP badge_status_timestamp;

DROP TABLE IF EXISTS badgemanagement.batch_badge;

DROP TABLE IF EXISTS badgemanagement.batch;

