
-- // BBB-1266-badge-indexing
-- Migration SQL that makes the change goes here.

create index concurrently holder_name_upper_trgm_ix ON badgemanagement.badge using gin (holder_name_upper public.gin_trgm_ops);

-- //@UNDO
-- SQL to undo the change goes here.
drop index badgemanagement.holder_name_upper_trgm_ix;

