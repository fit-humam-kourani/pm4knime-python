import dagre from "dagre";

const jsonStringFromJava =
  '{"nodes":[{"i_marking":true,"id":"e029b9773-f26c-4738-b93c-adcd43609c49","type":"place"},{"f_marking":true,"id":"e5ac55b55-6790-46b9-9429-e149b2fb8620","type":"place"},{"id":"e1b94b4d6-0aff-4fe4-9120-73146233b240","type":"place"},{"id":"e7d1a35a9-8dac-4b68-90a6-88021231cc59","type":"place"},{"id":"e349f28ab-34da-4879-ba37-85963e957cca","type":"place"},{"id":"e580bdef2-8e5e-4e9b-b616-76949783a396","type":"place"},{"id":"ebaa986d3-41cb-4349-a7dd-85456896b62a","type":"place"},{"id":"e43fc642d-383c-4ef5-b5ed-4ed13a490891","type":"place"},{"id":"e0375be02-875c-44ad-a41e-c5592872d1bb","type":"place"},{"id":"e68640582-fc41-417e-8b5e-7fedcaebbff3","type":"place"},{"id":"e79d80e62-9ad0-4527-8f16-4af565925245","type":"place"},{"id":"eb6a88e83-2094-4f43-b32f-5388108634f7","type":"place"},{"id":"e0c5820c6-b3db-4dbb-9076-056fef35358c","type":"place"},{"id":"ea2a08fac-dc22-4ca7-9778-543ead46500c","type":"place"},{"id":"e1d8d8420-a22c-4d11-9e75-3120dd695878","type":"place"},{"id":"ee042db6a-372c-42af-a7f3-6a968975275f","type":"place"},{"id":"ed083c439-d603-46ca-9adf-af9d5df7ddb1","type":"place"},{"id":"e0d17824e-5acb-4b0e-8a22-1dfe6759a8c3","type":"place"},{"id":"e2a794afe-2815-431a-a819-42b2e5a03d2b","type":"place"},{"id":"e1b5e5913-3340-44d5-8e3d-14f2885b9542","type":"place"},{"id":"eb9c6fd5d-941b-4ce5-b0ca-6dc9b382958b","type":"place"},{"id":"e6b7ac0e4-2b01-4a3d-9fa6-2d7699144def","type":"place"},{"id":"ed4b29a4f-573b-4c4c-b480-f2badfbb7bbd","type":"place"},{"id":"ed06eef5c-1325-4db6-b4da-a44325162406","type":"place"},{"id":"eef3c884e-d823-4b3b-9b65-8dbe8366cffb","type":"place"},{"id":"e566e44a0-259e-48eb-8fb2-b1098aeb57db","type":"place"},{"id":"e58900eee-cce6-4607-b665-ac883541cbfb","type":"place"},{"id":"e0fd898a2-e8ef-47ef-b8db-62a81eaef3b0","type":"place"},{"id":"ec8263745-32a5-443a-b384-00e17cde9550","type":"place"},{"id":"ed0dda194-b090-4de7-b949-774d89551fa9","type":"place"},{"id":"e244a32f0-cbf5-42aa-b921-f17d36e02b25","type":"place"},{"id":"e94b247cb-bd80-4d60-ab1a-b5b8f5f16cda","label":"Insert ticket","type":"transition"},{"id":"ec31a001a-0b02-4f95-9f5f-d6d9efea8d63","type":"transition"},{"id":"e8aa006dc-911a-4355-9060-2221e221364f","type":"transition"},{"id":"ea5e1c422-cccb-4f25-b557-f2bb2368afaf","label":"Assign seriousness","type":"transition"},{"id":"e2de00fa4-3b42-4fda-86dd-08395dfc4160","type":"transition"},{"id":"e4dabacbe-fd72-4d22-b23c-fb87dff35627","type":"transition"},{"id":"e8e0bac7d-345a-4da1-bec1-993e5ea68b4a","type":"transition"},{"id":"ef32f68ee-1a6a-4a76-890c-0f05ec5188a2","type":"transition"},{"id":"e4edeb4d8-d587-4b61-93e3-427122e302c7","label":"Create SW anomaly","type":"transition"},{"id":"ea8fe7b95-f399-404f-8b1b-2b7a33e080ac","type":"transition"},{"id":"e3946e541-94eb-4ccf-a710-f7bfc5fbd1af","type":"transition"},{"id":"ea00bf53d-8050-4ce3-9b8a-7dac30c0028a","type":"transition"},{"id":"e29bc0ffa-0a9e-4d87-8ffd-70689ce47206","type":"transition"},{"id":"e625e371e-c33e-4234-9067-0508c02e02cb","label":"Take in charge ticket","type":"transition"},{"id":"e617fcbf9-4f18-4122-9853-3b75e3760ffb","label":"Schedule intervention","type":"transition"},{"id":"e96b2979c-9777-4392-82ad-57fd06b67c6c","type":"transition"},{"id":"ef4b5d463-19ff-4fc1-b77c-0b02c54a826d","type":"transition"},{"id":"e79226b7c-3d53-4aef-a96d-f2eba4435393","type":"transition"},{"id":"ea6b51623-1132-4e38-9c90-c205fb6a2c61","type":"transition"},{"id":"e7f6ed1c0-93c4-42e5-a558-f3219a389e8e","type":"transition"},{"id":"e4122d984-b744-40ae-ac62-e2038eb29bd0","label":"Resolve SW anomaly","type":"transition"},{"id":"eae1337fc-d216-472e-a717-eaaff84070c9","type":"transition"},{"id":"ea0eab2b6-984a-40a0-8880-95f574e0b2cb","type":"transition"},{"id":"ef3e63dc6-ca40-41b5-a011-42dc5bfe35c0","type":"transition"},{"id":"ecc74f3d5-8f09-4c31-b53d-4210eb6d8860","label":"Wait","type":"transition"},{"id":"e70eb0d6d-3409-4817-bdf1-2513f70b0319","type":"transition"},{"id":"e3c40bb39-7d78-4273-9e32-8f8205108b8c","type":"transition"},{"id":"efeb5f8d8-7c99-4422-829e-91ed13ff8095","label":"Require upgrade","type":"transition"},{"id":"e6c459989-67cc-4838-a292-c385d22749ca","type":"transition"},{"id":"e634f1140-33ee-43f1-a009-aa7aebd88b25","type":"transition"},{"id":"ee5108b02-7411-40eb-80c7-9eae33260ec7","type":"transition"},{"id":"ed81547d0-2a07-4f09-a98d-0c8ee0dc4b90","label":"VERIFIED","type":"transition"},{"id":"e72985955-884a-452a-bbfa-731b44c1699d","type":"transition"},{"id":"e8857a124-1b90-445c-932e-c5b06c66f910","label":"DUPLICATE","type":"transition"},{"id":"eed36a159-715e-4ba8-b08d-22b1c113b3ce","type":"transition"},{"id":"e7f76f84b-de1f-4aba-b6a9-7524b6b2f0e9","label":"Resolve ticket","type":"transition"},{"id":"e1a78b5e8-96dc-4668-961f-29447131a0f0","type":"transition"},{"id":"e53e7e6bc-20b3-4f90-81bd-6ddb202efcbb","label":"RESOLVED","type":"transition"},{"id":"ed7e62a1c-f11e-4984-bdd5-6e49dd99efca","type":"transition"},{"id":"eb4836b24-31f6-419c-8ae9-384e31caa832","label":"INVALID","type":"transition"},{"id":"ee98c2cc4-b662-43e1-b33a-80e4f3696318","type":"transition"},{"id":"ecf9eb58a-4e6e-4aa3-bd52-1227ee5c8452","type":"transition"},{"id":"e114b5602-c3ae-4379-84d9-a316eba9c8e6","type":"transition"},{"id":"eb1775d11-2eb5-42f8-8a28-dc725303c6ec","type":"transition"},{"id":"e0276876f-4165-42c3-be70-da7dafe8ed92","label":"Closed","type":"transition"},{"id":"e3e9ce5e7-46dc-479f-bd8d-bd047acca511","type":"transition"},{"id":"efaa31bd9-8919-4ebe-a660-ad2c66b19180","type":"transition"},{"id":"e446de3c6-193c-46cf-a551-d1c8fd7d1389","type":"transition"},{"id":"ecd00b400-aa8e-4f22-a261-760133729f66","type":"transition"},{"id":"eab582ee7-245d-4fd2-8dfa-977fc74ad346","type":"transition"},{"id":"e685449c5-2efd-4e53-8129-f83e7631741a","type":"transition"},{"id":"ef8452ee3-1bbb-4b54-bf0e-8368562cbff3","type":"transition"},{"id":"e3b349d8b-833f-4528-a924-0613f50e8666","type":"transition"},{"id":"e68abfe27-2fa8-4aea-a424-96ee326ecd7b","type":"transition"},{"id":"e99d3cfb5-495b-4a92-916f-062406bc98e0","type":"transition"},{"id":"e95a7f175-d1a9-4fcf-bcbd-f2498873268e","type":"transition"}],"links":[{"source":"ee98c2cc4-b662-43e1-b33a-80e4f3696318","target":"ed06eef5c-1325-4db6-b4da-a44325162406"},{"source":"ea6b51623-1132-4e38-9c90-c205fb6a2c61","target":"eb6a88e83-2094-4f43-b32f-5388108634f7"},{"source":"e94b247cb-bd80-4d60-ab1a-b5b8f5f16cda","target":"e1b94b4d6-0aff-4fe4-9120-73146233b240"},{"source":"ecd00b400-aa8e-4f22-a261-760133729f66","target":"e6b7ac0e4-2b01-4a3d-9fa6-2d7699144def"},{"source":"eae1337fc-d216-472e-a717-eaaff84070c9","target":"ed083c439-d603-46ca-9adf-af9d5df7ddb1"},{"source":"eb6a88e83-2094-4f43-b32f-5388108634f7","target":"ef3e63dc6-ca40-41b5-a011-42dc5bfe35c0"},{"source":"e244a32f0-cbf5-42aa-b921-f17d36e02b25","target":"e0276876f-4165-42c3-be70-da7dafe8ed92"},{"source":"ed4b29a4f-573b-4c4c-b480-f2badfbb7bbd","target":"e446de3c6-193c-46cf-a551-d1c8fd7d1389"},{"source":"e1b5e5913-3340-44d5-8e3d-14f2885b9542","target":"e634f1140-33ee-43f1-a009-aa7aebd88b25"},{"source":"e68640582-fc41-417e-8b5e-7fedcaebbff3","target":"ea8fe7b95-f399-404f-8b1b-2b7a33e080ac"},{"source":"ea0eab2b6-984a-40a0-8880-95f574e0b2cb","target":"e43fc642d-383c-4ef5-b5ed-4ed13a490891"},{"source":"e685449c5-2efd-4e53-8129-f83e7631741a","target":"e0d17824e-5acb-4b0e-8a22-1dfe6759a8c3"},{"source":"ea8fe7b95-f399-404f-8b1b-2b7a33e080ac","target":"e79d80e62-9ad0-4527-8f16-4af565925245"},{"source":"e58900eee-cce6-4607-b665-ac883541cbfb","target":"e7f76f84b-de1f-4aba-b6a9-7524b6b2f0e9"},{"source":"eb1775d11-2eb5-42f8-8a28-dc725303c6ec","target":"e244a32f0-cbf5-42aa-b921-f17d36e02b25"},{"source":"e566e44a0-259e-48eb-8fb2-b1098aeb57db","target":"eed36a159-715e-4ba8-b08d-22b1c113b3ce"},{"source":"e43fc642d-383c-4ef5-b5ed-4ed13a490891","target":"e68abfe27-2fa8-4aea-a424-96ee326ecd7b"},{"source":"e566e44a0-259e-48eb-8fb2-b1098aeb57db","target":"e8857a124-1b90-445c-932e-c5b06c66f910"},{"source":"ed06eef5c-1325-4db6-b4da-a44325162406","target":"ecf9eb58a-4e6e-4aa3-bd52-1227ee5c8452"},{"source":"e68640582-fc41-417e-8b5e-7fedcaebbff3","target":"e3946e541-94eb-4ccf-a710-f7bfc5fbd1af"},{"source":"eb9c6fd5d-941b-4ce5-b0ca-6dc9b382958b","target":"eab582ee7-245d-4fd2-8dfa-977fc74ad346"},{"source":"e29bc0ffa-0a9e-4d87-8ffd-70689ce47206","target":"ea2a08fac-dc22-4ca7-9778-543ead46500c"},{"source":"e3e9ce5e7-46dc-479f-bd8d-bd047acca511","target":"e244a32f0-cbf5-42aa-b921-f17d36e02b25"},{"source":"ef3e63dc6-ca40-41b5-a011-42dc5bfe35c0","target":"e43fc642d-383c-4ef5-b5ed-4ed13a490891"},{"source":"e349f28ab-34da-4879-ba37-85963e957cca","target":"e70eb0d6d-3409-4817-bdf1-2513f70b0319"},{"source":"eb6a88e83-2094-4f43-b32f-5388108634f7","target":"e7f6ed1c0-93c4-42e5-a558-f3219a389e8e"},{"source":"e114b5602-c3ae-4379-84d9-a316eba9c8e6","target":"ed4b29a4f-573b-4c4c-b480-f2badfbb7bbd"},{"source":"e3b349d8b-833f-4528-a924-0613f50e8666","target":"e7d1a35a9-8dac-4b68-90a6-88021231cc59"},{"source":"e029b9773-f26c-4738-b93c-adcd43609c49","target":"e94b247cb-bd80-4d60-ab1a-b5b8f5f16cda"},{"source":"e625e371e-c33e-4234-9067-0508c02e02cb","target":"e1d8d8420-a22c-4d11-9e75-3120dd695878"},{"source":"e0c5820c6-b3db-4dbb-9076-056fef35358c","target":"e79226b7c-3d53-4aef-a96d-f2eba4435393"},{"source":"e349f28ab-34da-4879-ba37-85963e957cca","target":"ef32f68ee-1a6a-4a76-890c-0f05ec5188a2"},{"source":"e72985955-884a-452a-bbfa-731b44c1699d","target":"e566e44a0-259e-48eb-8fb2-b1098aeb57db"},{"source":"e68abfe27-2fa8-4aea-a424-96ee326ecd7b","target":"e7d1a35a9-8dac-4b68-90a6-88021231cc59"},{"source":"eef3c884e-d823-4b3b-9b65-8dbe8366cffb","target":"ed81547d0-2a07-4f09-a98d-0c8ee0dc4b90"},{"source":"e1a78b5e8-96dc-4668-961f-29447131a0f0","target":"e0fd898a2-e8ef-47ef-b8db-62a81eaef3b0"},{"source":"e6b7ac0e4-2b01-4a3d-9fa6-2d7699144def","target":"ee5108b02-7411-40eb-80c7-9eae33260ec7"},{"source":"e2a794afe-2815-431a-a819-42b2e5a03d2b","target":"e6c459989-67cc-4838-a292-c385d22749ca"},{"source":"ed0dda194-b090-4de7-b949-774d89551fa9","target":"e3e9ce5e7-46dc-479f-bd8d-bd047acca511"},{"source":"e3946e541-94eb-4ccf-a710-f7bfc5fbd1af","target":"e0375be02-875c-44ad-a41e-c5592872d1bb"},{"source":"e70eb0d6d-3409-4817-bdf1-2513f70b0319","target":"e43fc642d-383c-4ef5-b5ed-4ed13a490891"},{"source":"e99d3cfb5-495b-4a92-916f-062406bc98e0","target":"e1b94b4d6-0aff-4fe4-9120-73146233b240"},{"source":"efeb5f8d8-7c99-4422-829e-91ed13ff8095","target":"e1b5e5913-3340-44d5-8e3d-14f2885b9542"},{"source":"e0d17824e-5acb-4b0e-8a22-1dfe6759a8c3","target":"ef8452ee3-1bbb-4b54-bf0e-8368562cbff3"},{"source":"e1b94b4d6-0aff-4fe4-9120-73146233b240","target":"e8e0bac7d-345a-4da1-bec1-993e5ea68b4a"},{"source":"e53e7e6bc-20b3-4f90-81bd-6ddb202efcbb","target":"ec8263745-32a5-443a-b384-00e17cde9550"},{"source":"e95a7f175-d1a9-4fcf-bcbd-f2498873268e","target":"e5ac55b55-6790-46b9-9429-e149b2fb8620"},{"source":"e1d8d8420-a22c-4d11-9e75-3120dd695878","target":"e96b2979c-9777-4392-82ad-57fd06b67c6c"},{"source":"ed06eef5c-1325-4db6-b4da-a44325162406","target":"e114b5602-c3ae-4379-84d9-a316eba9c8e6"},{"source":"e8857a124-1b90-445c-932e-c5b06c66f910","target":"e58900eee-cce6-4607-b665-ac883541cbfb"},{"source":"ef8452ee3-1bbb-4b54-bf0e-8368562cbff3","target":"e2a794afe-2815-431a-a819-42b2e5a03d2b"},{"source":"e4edeb4d8-d587-4b61-93e3-427122e302c7","target":"e68640582-fc41-417e-8b5e-7fedcaebbff3"},{"source":"ec8263745-32a5-443a-b384-00e17cde9550","target":"ee98c2cc4-b662-43e1-b33a-80e4f3696318"},{"source":"e617fcbf9-4f18-4122-9853-3b75e3760ffb","target":"e0c5820c6-b3db-4dbb-9076-056fef35358c"},{"source":"e349f28ab-34da-4879-ba37-85963e957cca","target":"ea00bf53d-8050-4ce3-9b8a-7dac30c0028a"},{"source":"ed4b29a4f-573b-4c4c-b480-f2badfbb7bbd","target":"eb1775d11-2eb5-42f8-8a28-dc725303c6ec"},{"source":"eef3c884e-d823-4b3b-9b65-8dbe8366cffb","target":"e72985955-884a-452a-bbfa-731b44c1699d"},{"source":"e96b2979c-9777-4392-82ad-57fd06b67c6c","target":"e0c5820c6-b3db-4dbb-9076-056fef35358c"},{"source":"eab582ee7-245d-4fd2-8dfa-977fc74ad346","target":"e0d17824e-5acb-4b0e-8a22-1dfe6759a8c3"},{"source":"ee5108b02-7411-40eb-80c7-9eae33260ec7","target":"eef3c884e-d823-4b3b-9b65-8dbe8366cffb"},{"source":"e4dabacbe-fd72-4d22-b23c-fb87dff35627","target":"e349f28ab-34da-4879-ba37-85963e957cca"},{"source":"ed7e62a1c-f11e-4984-bdd5-6e49dd99efca","target":"ec8263745-32a5-443a-b384-00e17cde9550"},{"source":"e1b94b4d6-0aff-4fe4-9120-73146233b240","target":"e8aa006dc-911a-4355-9060-2221e221364f"},{"source":"e634f1140-33ee-43f1-a009-aa7aebd88b25","target":"e6b7ac0e4-2b01-4a3d-9fa6-2d7699144def"},{"source":"e0d17824e-5acb-4b0e-8a22-1dfe6759a8c3","target":"e3b349d8b-833f-4528-a924-0613f50e8666"},{"source":"e43fc642d-383c-4ef5-b5ed-4ed13a490891","target":"e3c40bb39-7d78-4273-9e32-8f8205108b8c"},{"source":"e2a794afe-2815-431a-a819-42b2e5a03d2b","target":"efeb5f8d8-7c99-4422-829e-91ed13ff8095"},{"source":"ec31a001a-0b02-4f95-9f5f-d6d9efea8d63","target":"e1b94b4d6-0aff-4fe4-9120-73146233b240"},{"source":"eb4836b24-31f6-419c-8ae9-384e31caa832","target":"ed06eef5c-1325-4db6-b4da-a44325162406"},{"source":"ec8263745-32a5-443a-b384-00e17cde9550","target":"eb4836b24-31f6-419c-8ae9-384e31caa832"},{"source":"ea00bf53d-8050-4ce3-9b8a-7dac30c0028a","target":"e0375be02-875c-44ad-a41e-c5592872d1bb"},{"source":"ee042db6a-372c-42af-a7f3-6a968975275f","target":"ea0eab2b6-984a-40a0-8880-95f574e0b2cb"},{"source":"e0375be02-875c-44ad-a41e-c5592872d1bb","target":"e29bc0ffa-0a9e-4d87-8ffd-70689ce47206"},{"source":"e349f28ab-34da-4879-ba37-85963e957cca","target":"ecc74f3d5-8f09-4c31-b53d-4210eb6d8860"},{"source":"ed083c439-d603-46ca-9adf-af9d5df7ddb1","target":"e4122d984-b744-40ae-ac62-e2038eb29bd0"},{"source":"e7f6ed1c0-93c4-42e5-a558-f3219a389e8e","target":"ed083c439-d603-46ca-9adf-af9d5df7ddb1"},{"source":"e0375be02-875c-44ad-a41e-c5592872d1bb","target":"ea6b51623-1132-4e38-9c90-c205fb6a2c61"},{"source":"ed81547d0-2a07-4f09-a98d-0c8ee0dc4b90","target":"e566e44a0-259e-48eb-8fb2-b1098aeb57db"},{"source":"ecf9eb58a-4e6e-4aa3-bd52-1227ee5c8452","target":"eef3c884e-d823-4b3b-9b65-8dbe8366cffb"},{"source":"e7d1a35a9-8dac-4b68-90a6-88021231cc59","target":"e99d3cfb5-495b-4a92-916f-062406bc98e0"},{"source":"ea2a08fac-dc22-4ca7-9778-543ead46500c","target":"e625e371e-c33e-4234-9067-0508c02e02cb"},{"source":"e0c5820c6-b3db-4dbb-9076-056fef35358c","target":"ef4b5d463-19ff-4fc1-b77c-0b02c54a826d"},{"source":"e7f76f84b-de1f-4aba-b6a9-7524b6b2f0e9","target":"e0fd898a2-e8ef-47ef-b8db-62a81eaef3b0"},{"source":"e6c459989-67cc-4838-a292-c385d22749ca","target":"e1b5e5913-3340-44d5-8e3d-14f2885b9542"},{"source":"e029b9773-f26c-4738-b93c-adcd43609c49","target":"ec31a001a-0b02-4f95-9f5f-d6d9efea8d63"},{"source":"eed36a159-715e-4ba8-b08d-22b1c113b3ce","target":"e58900eee-cce6-4607-b665-ac883541cbfb"},{"source":"e1b5e5913-3340-44d5-8e3d-14f2885b9542","target":"e685449c5-2efd-4e53-8129-f83e7631741a"},{"source":"ecc74f3d5-8f09-4c31-b53d-4210eb6d8860","target":"e43fc642d-383c-4ef5-b5ed-4ed13a490891"},{"source":"e3c40bb39-7d78-4273-9e32-8f8205108b8c","target":"e2a794afe-2815-431a-a819-42b2e5a03d2b"},{"source":"e580bdef2-8e5e-4e9b-b616-76949783a396","target":"e2de00fa4-3b42-4fda-86dd-08395dfc4160"},{"source":"eb9c6fd5d-941b-4ce5-b0ca-6dc9b382958b","target":"ecd00b400-aa8e-4f22-a261-760133729f66"},{"source":"e7d1a35a9-8dac-4b68-90a6-88021231cc59","target":"e95a7f175-d1a9-4fcf-bcbd-f2498873268e"},{"source":"e79d80e62-9ad0-4527-8f16-4af565925245","target":"e4edeb4d8-d587-4b61-93e3-427122e302c7"},{"source":"e0fd898a2-e8ef-47ef-b8db-62a81eaef3b0","target":"ed7e62a1c-f11e-4984-bdd5-6e49dd99efca"},{"source":"efaa31bd9-8919-4ebe-a660-ad2c66b19180","target":"eb9c6fd5d-941b-4ce5-b0ca-6dc9b382958b"},{"source":"e8aa006dc-911a-4355-9060-2221e221364f","target":"ebaa986d3-41cb-4349-a7dd-85456896b62a"},{"source":"e0fd898a2-e8ef-47ef-b8db-62a81eaef3b0","target":"e53e7e6bc-20b3-4f90-81bd-6ddb202efcbb"},{"source":"ed0dda194-b090-4de7-b949-774d89551fa9","target":"efaa31bd9-8919-4ebe-a660-ad2c66b19180"},{"source":"e0276876f-4165-42c3-be70-da7dafe8ed92","target":"ed0dda194-b090-4de7-b949-774d89551fa9"},{"source":"ebaa986d3-41cb-4349-a7dd-85456896b62a","target":"ea5e1c422-cccb-4f25-b557-f2bb2368afaf"},{"source":"e2de00fa4-3b42-4fda-86dd-08395dfc4160","target":"ebaa986d3-41cb-4349-a7dd-85456896b62a"},{"source":"ee042db6a-372c-42af-a7f3-6a968975275f","target":"eae1337fc-d216-472e-a717-eaaff84070c9"},{"source":"e4122d984-b744-40ae-ac62-e2038eb29bd0","target":"ee042db6a-372c-42af-a7f3-6a968975275f"},{"source":"ea5e1c422-cccb-4f25-b557-f2bb2368afaf","target":"e580bdef2-8e5e-4e9b-b616-76949783a396"},{"source":"e580bdef2-8e5e-4e9b-b616-76949783a396","target":"e4dabacbe-fd72-4d22-b23c-fb87dff35627"},{"source":"e79226b7c-3d53-4aef-a96d-f2eba4435393","target":"eb6a88e83-2094-4f43-b32f-5388108634f7"},{"source":"ef32f68ee-1a6a-4a76-890c-0f05ec5188a2","target":"e79d80e62-9ad0-4527-8f16-4af565925245"},{"source":"e8e0bac7d-345a-4da1-bec1-993e5ea68b4a","target":"e349f28ab-34da-4879-ba37-85963e957cca"},{"source":"ef4b5d463-19ff-4fc1-b77c-0b02c54a826d","target":"ea2a08fac-dc22-4ca7-9778-543ead46500c"},{"source":"e58900eee-cce6-4607-b665-ac883541cbfb","target":"e1a78b5e8-96dc-4668-961f-29447131a0f0"},{"source":"e1d8d8420-a22c-4d11-9e75-3120dd695878","target":"e617fcbf9-4f18-4122-9853-3b75e3760ffb"},{"source":"e446de3c6-193c-46cf-a551-d1c8fd7d1389","target":"eb9c6fd5d-941b-4ce5-b0ca-6dc9b382958b"}]}';
const jsonDataFromJava = JSON.parse(jsonStringFromJava); // Assuming jsonStringFromJava is your JSON string

const nodes = jsonDataFromJava.nodes;

const edges = jsonDataFromJava.links;

const createGraphElements = () => {
  const mainContainer = document.createElement("div");
  mainContainer.style.display = "flex";
  mainContainer.style.flexDirection = "column"; // Stack children vertically
  mainContainer.style.alignItems = "center"; // Center-align children horizontally
  mainContainer.style.justifyContent = "center";
  // mainContainer.style.background = "#f0ede6";
  mainContainer.style.flexDirection = "column";
  mainContainer.style.border = "1px solid #ccc";
  mainContainer.style.width = "1210px"; // Full width of the viewport
  // mainContainer.style.height = "100vh"; // Full height of the viewport

  const controlBar = document.createElement("div");
  controlBar.style.background = "#e0e0e0";
  controlBar.style.color = "#fff";
  controlBar.style.padding = "5px";
  controlBar.style.width = "1200px"; // Fixed width
  // controlBar.style.height = "100px";
  // controlBar.textContent = "Control Bar";
  controlBar.style.fontFamily = "Arial, sans-serif";

  // Create the graph container with fixed size
  const graphContainer = document.createElement("div");
  graphContainer.style.width = "100%"; // Fixed width
  graphContainer.style.height = "800px"; // Fixed height
  // graphContainer.style.border = "1px solid #ccc";
  // graphContainer.style.padding = "10px";
  mainContainer.style.background = "white";
  graphContainer.style.overflow = "auto"; // Enables scrollbars if content overflows
  graphContainer.style.margin = "auto";

  const paperDiv = document.createElement("div");
  paperDiv.id = "paper";
  paperDiv.style.width = "100%"; // Fixed width
  paperDiv.style.height = "100%";

  // Create zoom controls div
  const zoomControlsDiv = document.createElement("div");
  zoomControlsDiv.id = "zoom-controls";

  // Create zoom in and zoom out buttons
  const zoomInButton = document.createElement("button");
  zoomInButton.className = "zoom-button";
  zoomInButton.id = "zoom-in";
  zoomInButton.textContent = "Zoom In";

  const zoomOutButton = document.createElement("button");
  zoomOutButton.className = "zoom-button";
  zoomOutButton.id = "zoom-out";
  zoomOutButton.textContent = "Zoom Out";

  zoomInButton.textContent = "+";
  zoomOutButton.textContent = "-";

  // // Apply CSS styles for cool shapes
  // zoomInButton.style.fontSize = "24px"; // Large text size makes the "+" look more like a shape
  // zoomInButton.style.width = "50px"; // Square shape
  // zoomInButton.style.height = "50px";
  // zoomInButton.style.lineHeight = "50px"; // Center "+" vertically
  // zoomInButton.style.textAlign = "center"; // Center "+" horizontally
  // zoomInButton.style.borderRadius = "50%"; // Circular shape
  // zoomInButton.style.border = "2px solid #000"; // Solid border
  // zoomInButton.style.display = "inline-block";
  // zoomInButton.style.backgroundColor = "#fff"; // Background color
  // zoomInButton.style.cursor = "pointer"; // Change cursor on hover

  // // Apply the same styles to zoomOutButton, with adjustments as necessary
  // zoomOutButton.style = zoomInButton.style.cssText; // Copy styles from zoomInButton
  // zoomOutButton.textContent = "-";

  zoomControlsDiv.appendChild(zoomInButton);
  zoomControlsDiv.appendChild(zoomOutButton);

  // document.body.appendChild(zoomControlsDiv);
  // document.body.appendChild(paperDiv);

  controlBar.appendChild(zoomControlsDiv);

  graphContainer.appendChild(paperDiv);

  mainContainer.appendChild(controlBar);
  mainContainer.appendChild(graphContainer);

  // Append the main container to the body of the document
  document.body.appendChild(mainContainer);
};

createGraphElements();

function estimateTextWidth(text, fontSize) {
  if (typeof text === "undefined" || text === "") {
    return 0; // Return a default width or 0 if there's no text to measure
  }
  const averageCharWidth = fontSize * 0.7; // Adjust based on your font
  return text.length * averageCharWidth;
}

function adjustPaperSize(graph, paper) {
  let maxX = 0;
  let maxY = 0;

  graph.getElements().forEach((element) => {
    let position = element.position();
    let size = element.size();
    maxX = Math.max(maxX, position.x + size.width);
    maxY = Math.max(maxY, position.y + size.height);
  });

  paper.setDimensions(maxX + 100, maxY + 100);

  // let bbox = V(paper.viewport).bbox(true);
  // paper.scaleContentToFit({ padding: 10, bbox: bbox });
  // paper.centerContent();
}

// This function will check each set of three consecutive points to determine if they are collinear. If they are, the middle point can be removed. First and last points are always skipped.
function simplifyWaypoints(points) {
  let simplified = [];

  for (let i = 1; i < points.length - 1; i++) {
    let prev = points[i - 1];
    let curr = points[i];
    let next = points[i + 1];

    // Calculate the determinant of the matrix formed by three points
    // | 1  x1  y1 |
    // | 1  x2  y2 |  = 0 for collinear points
    // | 1  x3  y3 |
    // If determinant is zero, points are collinear

    let det =
      prev.x * (curr.y - next.y) +
      curr.x * (next.y - prev.y) +
      next.x * (prev.y - curr.y);

    if (Math.abs(det) > 1e-10) {
      // Use a small epsilon to handle floating point errors
      simplified.push(curr); // Keep current point if it's not collinear
    }
  }

  return simplified;
}

function applyAutoLayout(nodes, edges) {
  var g = new dagre.graphlib.Graph();

  g.setGraph({
    rankdir: "LR",
    marginx: 20,
    marginy: 20,
  });

  g.setDefaultEdgeLabel(function () {
    return {};
  });

  nodes.forEach((node) => {
    // const fontSize = 22;
    // const textWidth = estimateTextWidth(node.label, fontSize);
    // const transitionWidth = Math.max(textWidth + 10, 20);
    g.setNode(node.id, {
      width: node.width,
      height: node.height,
      label: node.label,
    });
  });

  edges.forEach((edge) => {
    g.setEdge(edge.source, edge.target, {
      label: edge.label,
      width: 0,
      height: 0,
    });
  });

  dagre.layout(g);

  g.nodes().forEach(function (v) {
    let node = g.node(v);
    elements[v].position(node.x - node.width / 2, node.y - node.height / 2);
    elements[v].resize(node.width, node.height);
  });

  g.edges().forEach(function (e) {
    let edge = g.edge(e);
    let sourceElement = elements[e.v];
    let targetElement = elements[e.w];

    if (sourceElement && targetElement) {
      let links = graph
        .getConnectedLinks(sourceElement, { outbound: true })
        .filter((link) => link.getTargetElement() === targetElement);

      if (links.length > 0) {
        let link = links[0];
        let waypoints = edge.points.map((point) => ({
          x: point.x,
          y: point.y,
        }));
        let simplifiedWaypoints = simplifyWaypoints(waypoints); // Simplify waypoints
        link.vertices(simplifiedWaypoints); // Update the vertices with the simplified waypoints
      } else {
        console.error("No link found between nodes", e.v, "and", e.w);
      }
    } else {
      console.error(
        "Source or target elements not found for nodes",
        e.v,
        "and",
        e.w
      );
    }
  });
}

var graph = new joint.dia.Graph();

var paper = new joint.dia.Paper({
  el: document.getElementById("paper"),
  width: "100%", // Fixed width
  height: "100%",
  // gridSize: 10,
  defaultAnchor: { name: "perpendicular" },
  defaultConnectionPoint: { name: "boundary" },
  model: graph,
});

var pn = joint.shapes.pn;

var elements = {};

// Add nodes
nodes.forEach(function (node) {
  var element;
  if (node.type === "place") {
    let attrs = {
      ".root": { stroke: "#6aa84f", "stroke-width": 2 },
      ".tokens > circle": { fill: "#38761d" },
    };
    let tokens = 0;

    if (node.f_marking === true) {
      attrs[".root"]["stroke-width"] = 4;
    }
    if (node.i_marking === true) {
      tokens = 1;
    }

    node.width = 50;
    node.height = 50;

    element = new pn.Place({
      position: node.position,
      attrs: attrs,
      tokens: tokens,
      size: { width: 50, height: 50 },
    });
  } else if (node.type === "transition") {
    const fontSize = 22; // Example font size
    const textWidth = estimateTextWidth(node.label, fontSize);
    const transitionWidth = Math.max(textWidth + 10, 20);
    node.width = transitionWidth;
    node.height = 50;
    element = new pn.Transition({
      position: node.position,
      size: { width: transitionWidth, height: 50 },
      attrs: {
        ".label": {
          text: node.label || "",
          fill: "black",
          "font-size": fontSize,
          "ref-x": 0.5,
          "ref-y": 0.5,
          "text-anchor": "middle",
          "y-alignment": "middle",
        },
        ".root": {
          fill: "#cfe2f3",
          stroke: "#3f77cf",
          "stroke-width": 2,
        },
      },
    });
  }
  graph.addCell(element);
  elements[node.id] = element;
});

edges.forEach(function (edge) {
  var link = new pn.Link({
    source: { id: elements[edge.source].id, selector: ".root" },
    target: { id: elements[edge.target].id, selector: ".root" },
    attrs: {
      ".connection": { stroke: "grey", "stroke-width": 3 },
      ".marker-target": { fill: "grey", stroke: "grey", "stroke-width": 2 },
    },
    // router: { name: "manhattan" },
    // connector: { name: "smooth" },
  });
  graph.addCell(link);
});
applyAutoLayout(nodes, edges);

adjustPaperSize(graph, paper);

const addZoomListeners = (paper) => {
  let zoomLevel = 1;

  const zoom = (zoomLevel) => {
    paper.scale(zoomLevel);
    paper.fitToContent({
      useModelGeometry: true,
      padding: 100 * zoomLevel,
      allowNewOrigin: "any",
    });
  };

  document.getElementById("zoom-in").addEventListener("click", () => {
    zoomLevel = Math.min(3, zoomLevel + 0.2);
    zoom(zoomLevel);
  });

  document.getElementById("zoom-out").addEventListener("click", () => {
    zoomLevel = Math.max(0.2, zoomLevel - 0.2);
    zoom(zoomLevel);
  });

  // Mouse wheel listener for zooming
  paper.el.addEventListener("wheel", (event) => {
    event.preventDefault(); // Prevent scrolling
    const delta = event.deltaY;
    // Determine zoom direction
    if (delta > 0) {
      // Zoom out
      zoomLevel = Math.max(0.2, zoomLevel - 0.2);
    } else if (delta < 0) {
      // Zoom in
      zoomLevel = Math.min(3, zoomLevel + 0.2);
    }
    zoom(zoomLevel);
  });

  paper.on("element:pointerup link:pointerup", (cellView) => {
    paper.fitToContent({
      useModelGeometry: true,
      padding: 100 * zoomLevel,
      allowNewOrigin: "any",
    });
  });
};

addZoomListeners(paper);
