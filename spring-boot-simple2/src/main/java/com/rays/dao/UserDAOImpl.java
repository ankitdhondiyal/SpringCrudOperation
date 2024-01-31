package com.rays.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.rays.dto.UserDTO;

@Repository
public class UserDAOImpl implements UserDAOInt {

	@PersistenceContext
	public EntityManager entityManager;

	public long add(UserDTO dto) {
		entityManager.persist(dto);
		return dto.getId();
	}

	public void update(UserDTO dto) {
		entityManager.merge(dto);
	}

	public void delete(UserDTO dto) {
		entityManager.remove(dto);
	}

	@Override
	public UserDTO findByPk(long pk) {
		UserDTO dto = entityManager.find(UserDTO.class, pk);
		return dto;
	}

	public List search(UserDTO dto, int pageNo, int pageSize) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserDTO> cq = builder.createQuery(UserDTO.class);
		Root<UserDTO> qroot = cq.from(UserDTO.class);
		List<Predicate> predicateList= new ArrayList<Predicate>();
		
		if(dto!=null) {
			if(dto.getFirstName()!= null&& dto.getFirstName().length()>0) {
				predicateList.add(builder.like(qroot.get("firstName"), dto.getFirstName()));
			}
			
		}
		cq.where(predicateList.toArray(new Predicate[predicateList.size()]));

		TypedQuery<UserDTO> tq = entityManager.createQuery(cq);

		if (pageSize > 0) {
			tq.setFirstResult(pageNo * pageSize);
			tq.setMaxResults(pageSize);
		}

		List<UserDTO> list = tq.getResultList();

		return list;
	}

}