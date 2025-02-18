INSERT INTO public.tbl_wallet (id, balance, wallet_type)
VALUES (2, 500.00, 2);
INSERT INTO public.tbl_wallet (id, balance, wallet_type)
VALUES (1, 150.00, 1);
INSERT INTO public.tbl_wallet (id, balance, wallet_type)
VALUES (3, 1150.00, 1);

INSERT INTO public.tbl_shopkeeper (id, full_name, cnpj, email, password, wallet_id)
VALUES (1, 'shop', '12345666666666', 'geovane@mail.com', '1312312', 2);

INSERT INTO public.tbl_common (id, full_name, cpf, email, password, wallet_id)
VALUES (1, 'gabriel', '12345123451', 'gabriel@mail', '312321', 1);
INSERT INTO public.tbl_common (id, full_name, cpf, email, password, wallet_id)
VALUES (2, 'jonejone', '31232132456', 'jone@mail', '123123', 3);