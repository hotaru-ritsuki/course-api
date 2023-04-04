companyunit_channel_id integer default nextval('core.companyunit_channel_id_seq') not null,
	companyunit_id integer not null,
	channel_id integer not null,

	updated_timestamp timestamp with time zone default current_timestamp,
	import_pvi varchar,
	constraint fk_companyunit_channel_companyunit_id foreign key (companyunit_id)
		references core.companyunit(companyunit_id),
	constraint fk_companyunit_channel_channel_id foreign key (channel_id)
		references core.channel(channel_id),
	constraint pk_companyunit_channel primary key (companyunit_channel_id)