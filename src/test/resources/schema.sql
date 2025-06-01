CREATE SCHEMA IF NOT EXISTS superapp;
SET SCHEMA superapp;

-- فرض بر این است که بسته‌ی H2GIS روی classpath تست هست
CREATE ALIAS IF NOT EXISTS SPATIAL_INIT FOR "org.h2gis.h2spatialext.CreateSpatialExtension.initSpatialExtension";
CALL SPATIAL_INIT();
