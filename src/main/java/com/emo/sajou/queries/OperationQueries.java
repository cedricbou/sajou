package com.emo.sajou.queries;

import java.util.List;

import com.emo.mango.cqs.Queries;
import com.emo.mango.cqs.queries.annotations.MangoSql;
import com.emo.mango.cqs.queries.annotations.QueryBind;

public interface OperationQueries extends Queries {

/*
select o.creationDate, o.couleurs, o.debit, o.credit 
from (
	select op.creationDate as creationDate, servicesAsString as couleurs, op.montant as debit, '' as credit from operation op where compte = :compte 
		union
	select c.creationDate as creationDate, servicesAsString as couleurs, '' as debit, c.solde as credit from cartouche c where compte = :compte and validite >= current_date()
		union
	select cast(date_add(c.validite, interval 1 day) as datetime) as creationDate, servicesAsString as couleurs, c.solde as debit, '' as credit from cartouche c where compte = :compte and validite < current_date()
) as o order by o.creationDate desc;
 */
	@MangoSql(value="from (select op.eid as id, op.creationDate as creationDate, usage_servicesAsString as couleurs, op.montant as debit, '' as credit from operation op where numerocompte_compte = :compte union select c.eid as id, c.creationDate as creationDate, usage_servicesAsString as couleurs, '' as debit, c.soldeinitial_solde as credit from cartouche c where numerocompte_compte = :compte and validite >= current_date() union select c.eid as id, cast(date_add(c.validite, interval 1 day) as datetime) as creationDate, usage_servicesAsString as couleurs, c.solde_solde as debit, '' as credit from cartouche c where numerocompte_compte = :compte and validite < current_date()) as o order by o.creationDate desc")
	public List<OperationSummary> allOperationsForCompte(final @QueryBind("compte") String compte);
}
