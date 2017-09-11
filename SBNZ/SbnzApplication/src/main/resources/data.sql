-- Buyer = 0
-- Seller = 1
-- Manager = 2

insert into base_user 	(id		,name	,surname	,username	,password	,role	,registry_date	)
values					(null	,'Kristina','Pomorisac'	,'kris'	,'123'		,2		,1116633600000);

insert into base_user 	(id		,name	,surname	,username	,password	,role	,registry_date	)
values					(null	,'Rafa','Rafic'		,'rafa'		,'123'		,1		,1220054400000);

insert into base_user 	(id		,name	,surname	,username	,password	,role	,registry_date	)
values					(null	,'Pera','Peric'		,'pera'		,'123'		,0		,1326585600000);

insert into base_user 	(id		,name	,surname	,username	,password	,role	,registry_date	)
values					(null	,'Tina','Tinic'		,'tina'		,'123'		,0		,1382313600000);


-- buyer category insterts
insert into buyer_category 	(buyer_category_id	,category_name	,points_percentage	,spending_bound_max	,spending_bound_min)
values 						(1					,'GRAY'			,0					,0					,0);

insert into buyer_category 	(buyer_category_id	,category_name	,points_percentage	,spending_bound_max	,spending_bound_min)
values 						(2					,'BRONZE'		,5					,3000				,1500);

insert into buyer_category 	(buyer_category_id	,category_name	,points_percentage	,spending_bound_max	,spending_bound_min)
values 						(3					,'SILVER'		,15					,5000				,1000);

insert into buyer_category 	(buyer_category_id	,category_name	,points_percentage	,spending_bound_max	,spending_bound_min)
values 						(4					,'GOLD'			,30					,7000				,500);

-- buyer inserts
insert into buyer 	(id	,address			,reward_points	,b_category_id)
values 				(3	,'Bulevar Oslobodjenja 123'	,300	,1);
insert into buyer 	(id	,address			,reward_points	,b_category_id)
values 				(4	,'Jevrejska 7'		,125			,3);


-- product categories

-- Electronics, BASIC
insert into product_category	(id	,category_name		,max_discount	,parent_category_id)
values 							(1	,'Electronics'		,20				,null);

insert into product_category	(id	,category_name		,max_discount	,parent_category_id)
values 							(2	,'Television'		,30				,1);

insert into product_category	(id	,category_name		,max_discount	,parent_category_id)
values 							(3	,'Laptop Computers'	,25				,1);

insert into product_category	(id	,category_name		,max_discount	,parent_category_id)
values 							(4	,'PC'				,25				,1);

-- Gadgets
insert into product_category	(id	,category_name	,max_discount	,parent_category_id)
values							(5	,'Gadgets'		,40				,null);

insert into product_category	(id	,category_name	,max_discount	,parent_category_id)
values							(6	,'Microwave'	,25				,5);

insert into product_category	(id	,category_name	,max_discount	,parent_category_id)
values							(7	,'Vacuum',25			,5);

-- Wholesale
insert into product_category	(id	,category_name	,max_discount	,parent_category_id)
values							(8	,'Wholesale'	,40				,null);

-- product
-- television
insert into product (	id	,name,									amount_in_stock,archived,	date_created, 	min_amount_in_stock, 	price,	restock,	p_category_id)
values				(	null,'SENCOR LED SLE 1958',					1000,			0,			1220054400000,	200,					13320,	0,			2),
					(	null,'FOX 32DLE172',						600,			0,			1220054400000,	150,					19550,	0,			2),
					(	null,'VOX 32DIS470B',						120,			0,			1220054400000,	30,						19550,	0,			2),
-- laptops
					(	null,'ACER Aspire ES1-432-C5B4',			2000,			0,			1220054400000,	500,					29990,	0,			3),
					(	null,'ACER One 10 S1003-12X9',				2400,			0,			1220054400000,	400,					33990,	0,			3),
					(	null,'HP Spectre 13-v101nn',				600,			0,			1220054400000,	100,					188990,	0,			3),
-- PCs
					(	null,'GIGATRON AURORA LIDER',				300,			0,			1220054400000,	80,						29990,	0,			4),
					(	null,'GIGATRON PRIME PRO i300',				150,			0,			1220054400000,	30,						75990,	0,			4),

-- Microwave
					(	null,'VOX MWH-M22B',						1500,			0,			1220054400000,	200,					7490,	0,			6),
					(	null,'COLOSSUS CSS-5000B',					350,			0,			1220054400000,	100,					13490,	0,			6),

-- Vacuum cleaner
					(	null,'CLATRONIC HS 2631',					3400,			0,			1220054400000,	500,					4190,	0,			7),
					(	null,'VIVAX VC-1603',						500,			0,			1220054400000,	80,						4690,	0,			7),
					(	null,'DAEWOO RC-360R',						4200,			0,			1220054400000,	1000,					5990,	0,			7),
					(	null,'BAUER V.F. AQUA',						500,			0,			1220054400000,	80,						7690,	0,			7),
					(	null,'HOME ELECTRONICS VC-18002DB',			4200,			0,			1220054400000,	1000,					6990,	0,			7),

-- Wholescale
					(	null,'Bread',								8000,			0,			1220054400000,	300,					30,		0,			8),
					(	null,'Water',								10000,			0,			1220054400000,	1000,					45,		0,			8);
					
-- sales events
insert into sales_event (id		,start_date		,end_date		,event_discount	,event_name)
values				(null	,977353200000	,978217200000		,10				,'Chrismas Sale'),
					(null	,951951600000	,959810400000		,7				,'Easter Sale'),
					(null	,959896800000	,967759200000		,5				,'Summer Sale'),
					(null	,967845600000	,975625200000		,10				,'Back to Scool Sale'),
					(null	,968536800000	,968709600000		,3				,'Project Day');

insert into product_category_relation 	(	event_id	,product_category_id)
values 									(	1			,1),
										(	2			,1),
										(	3			,1),
										(	4			,1),
										(	5			,1),
										
										(	1			,2),
										(	2			,2),
										(	3			,2),
										(	4			,2),
										(	5			,2),
										
										(	1			,3),
										(	2			,3),
										(	3			,3),
										(	4			,3),
										(	5			,3),
										
										(	1			,4),
										(	2			,4),
										(	3			,4),
										(	4			,4),
										(	5			,4),
										
										(	1			,5),
										(	2			,5),
										(	3			,5),
										(	4			,5),
										(	5			,5),
									
										(	1			,6),
										(	2			,6),
										(	3			,6),
										(	4			,6),
										(	5			,6),

										(	1			,7),
										(	2			,7),
										(	3			,7),
										(	4			,7),
										(	5			,7),
										
										(	1			,8),
										(	2			,8),
										(	3			,8),
										(	4			,8),
										(	5			,8);

