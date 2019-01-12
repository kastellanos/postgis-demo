# Gestion de données à grande échelle

## Introduction aux Systèmes d'Information Géographiques – TP OpenStreetMap

Equipe:

- BENJELLOUN Ghita
- CASTELLANOS-PAEZ Andres
- EL HANNAOUI Sara
- LARAQUI Omar


### 2.1 Connexion à la base avec le client psql

**Question 1:**  
```
select disctinct count(*) from users
count
------
4576
```
### 2.2 Interrogation de base

**Question 2.a:** Quelles sont ses coordonnées géographiques?

```
select id,ST_X(geom),ST_Y(geom),tags from nodes where id=1787038609;

(5.7680106)
(45.192893)

id     |   st_x    |   st_y    |                        tags                        
------------+-----------+-----------+----------------------------------------------------
1787038609 | 5.7680106 | 45.192893 | "name"=>"Domaine Universitaire", "place"=>"suburb"
```

**Question 2.b:** Dans quel système de référence ces coordonnées sont-elles exprimées ?

```
EPSG


select id, ST_SRID(geom),tags from nodes where id=1787038609;               
     id     | st_srid |                        tags                        
------------+---------+----------------------------------------------------
 1787038609 |    4326 | "name"=>"Domaine Universitaire", "place"=>"suburb"


select srid, auth_name from spatial_ref_sys where srid=4326;

srid | auth_name
------+-----------
4326 | EPSG
(1 row)

```

### 2.3 Interrogation attributaire (hstore)

**Question 3 :**
```
SELECT tags,ST_X(ST_Centroid(bbox)), ST_Y(ST_Centroid(bbox)) FROM ways WHERE tags->'name' like '%Grenoble%' and tags->'amenity' = 'townhall';
                                                        tags                                                        |    st_x    |    st_y     
--------------------------------------------------------------------------------------------------------------------+------------+-------------
 "name"=>"Hôtel de Ville de Grenoble", "layer"=>"1", "amenity"=>"townhall", "building"=>"yes", "created_by"=>"JOSM" | 5.73644115 | 45.18644215

```

**Question 4 :**

```
SELECT tags->'highway' as highway_type,COUNT(*) FROM ways WHERE tags->'highway' like '%%' GROUP BY tags->'highway' ORDER BY COUNT(tags->'highway') DESC;

highway_type             | count
--------------------------+-------
residential              | 94356
unclassified             | 77678
service                  | 64461
track                    | 58276
tertiary                 | 21923
footway                  | 21742
path                     | 21413
secondary                | 18444
primary                  | 11970
pedestrian               |  4002
steps                    |  3355
cycleway                 |  2505
motorway                 |  2210
road                     |  1976
motorway_link            |  1956
living_street            |  1907
platform                 |  1821
trunk                    |  1045
```

### 2.4 Fonctions de mesure

**Question 5.a :**
```

SELECT tags->'highway' as highway_type, SUM( ST_Length(linestring)) as distance_degree
FROM ways WHERE tags->'highway' like '%%'
GROUP BY tags->'highway'
ORDER BY SUM( ST_Length(linestring)) DESC;

highway_type       |   distance_degree    
--------------------------+----------------------
unclassified             |     435.285712879271
track                    |     343.366696791791
residential              |     210.396982404602
tertiary                 |     208.294200085326
path                     |     134.367018999508
secondary                |     120.622408690095
service                  |     62.5645575726105
primary                  |     53.3422148486077
motorway                 |     28.6715936154116
footway                  |     26.9145189180964
cycleway                 |     10.1213381544952
road                     |     8.37733244501456
trunk                    |     7.09821321050042
motorway_link            |      5.5117793767586
pedestrian               |     4.73331637143004
bridleway                |     2.85057359795041
living_street            |     2.49893965509429
trunk_link               |     2.12683675252511


```
**Question 5.b :**

```
L'objet geometry(Geometry,4326) il est stocke en utilisant le système de référence EPSG 4326. Ce système il utilise comme coordonnées latitude et longitude et le systeme de measure est le degrèes
```

**Question 5.c :**

```

SELECT tags->'highway' as highway_type, SUM( ST_Length(ST_Transform(linestring,2100)))/1000 as distance_km
FROM ways WHERE tags->'highway' like '%%'
GROUP BY tags->'highway'
ORDER BY SUM( ST_Length(ST_Transform(linestring,2100)))/1000 DESC;

highway_type       |       distance_km     
--------------------------+--------------------
unclassified             |   40891.7393339919
track                    |   32373.8797317573
residential              |   19720.9099059278
tertiary                 |   19597.5465530888
path                     |   12689.5784904344
secondary                |   11346.1555113029
service                  |   5869.08324538568
primary                  |   5034.03222962346
motorway                 |   2714.02252599907
footway                  |   2515.91745205359
cycleway                 |     951.2521520049
road                     |   789.459645354349
trunk                    |   664.255628533118
motorway_link            |   518.855970603794
pedestrian               |   441.926412063013
bridleway                |   262.989454236282
living_street            |   235.221944533964
trunk_link               |   198.410323309958


```

**Question 6 :**

```
select w.tags->'name' as name, ST_Area(ST_Transform(w.bbox,2100)) as area_m2 from relations r, relation_members rm, ways w where r.tags->'name' = 'Ensimag' and r.id=rm.relation_id and w.id = rm.member_id order by ST_Area(ST_Transform(w.bbox,2100)) DESC;

name   |     area_m2      
-------------+------------------
Ensimag - D | 1507.30417257395
Ensimag     | 1084.43633523211
Ensimag - H | 742.992025473959
Ensimag - E | 687.736062417669

```

### 2.5 Intersections, etc.

**Question 7 :**

```
select q.quartier,w.tags->'name' from quartier q, ways w where w.tags->'amenity' ='school' and ST_Intersects(ST_Transform(the_geom,4326),bbox)=true;

quartier       |                       ?column?                       
----------------------+------------------------------------------------------
ABBAYE-JOUHAUX       | Groupe Scolaire Voltaire
ABBAYE-JOUHAUX       | École primaire Grand Châtelet
ABBAYE-JOUHAUX       | École primaire Jules Ferry
ABBAYE-JOUHAUX       | Lycée André Argouges
ABBAYE-JOUHAUX       | Collège Vercors
ABBAYE-JOUHAUX       | Groupe scolaire Léon Jouhaux
ALPINS-ALLIERS       | Lycée Guynemer
ALPINS-ALLIERS       | École Primaire Alphonse Daudet
ALPINS-ALLIERS       | Lycée Technique et Professionnel Privé Iser Bordier
ALPINS-ALLIERS       | École élémentaire Élisée Chatin
ALPINS-ALLIERS       | École élémentaire Beauvert
ALPINS-ALLIERS       | Groupe scolaire Sidi-Brahim
ALPINS-ALLIERS       | Groupe Scolaire Alphonse Daudet
BERRIAT ST BRUNO     | Lycée International Europole
BERRIAT ST BRUNO     | Collège Fantin-Latour
BERRIAT ST BRUNO     | Groupe Scolaire Nicolas Chorier
BERRIAT ST BRUNO     |
BERRIAT ST BRUNO     | Externat Saint-Bruno

```
