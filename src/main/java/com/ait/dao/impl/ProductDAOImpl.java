package com.ait.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.ait.dao.ProductDAO;
import com.ait.entity.ProductEntity;

public class ProductDAOImpl implements ProductDAO {

	private EntityManagerFactory factory;
	
	public ProductDAOImpl() {
		factory = Persistence.createEntityManagerFactory("test");
	}
	

	@Override
	public ProductEntity saveProduct(ProductEntity product) {
		EntityManager entityManager = factory.createEntityManager();
		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();
		
		try {
			entityManager.persist(product);
			tx.commit();
			System.out.println("Product is inserted to the Database");
		}
		catch(Exception ex) {
			tx.rollback();
			System.out.println("Error in inserting a product" + ex);
		}
		finally {
			entityManager.close();
		}
		
		
		return product;
	}

	@Override
	public ProductEntity loadProductById(Integer productId) {
		EntityManager entityManager = factory.createEntityManager();
		
		/*
		 * find() : early load
		 * getReference() : lazy load
		 */
		ProductEntity p = entityManager.find(ProductEntity.class, productId);
		
		entityManager.close();
		return p;
	}

	@Override
	public ProductEntity updateProductById(Integer productId, Double new_unitPrice) {
		EntityManager entityManager = factory.createEntityManager();
		ProductEntity p = entityManager.find(ProductEntity.class, productId);
		EntityTransaction tx = entityManager.getTransaction();
		
		tx.begin();
		p.setUnitprice(new_unitPrice);
		tx.commit();
		entityManager.close();
		return p;
	}

	@Override
	public void deleteProductById(Integer productId) {
		EntityManager entityManager = factory.createEntityManager();
		ProductEntity p = entityManager.find(ProductEntity.class, productId);
		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();
		entityManager.remove(p);
		tx.commit();
		entityManager.close();
		
	}


	@Override
	public void testEntityStates() {
		EntityManager entityManager = factory.createEntityManager();
		/*
		 * create a new product
		 */
		/*
		ProductEntity pe = new ProductEntity(); // transient state
		pe.setProductId(27620);
		pe.setProductName("Earbuds");
		pe.setQuantity(5);
		pe.setUnitprice(2400.0);*/
		
		ProductEntity pe = entityManager.find(ProductEntity.class, 27620);
		// pe entity is in persistent state
		
		/*
		EntityTransaction tx = entityManager.getTransaction();
		tx.begin();
	    entityManager.persist(pe);   // persistent state
		tx.commit();
		*/
		
		entityManager.detach(pe);  // detached state
		pe.setUnitprice(3100.0); // The change made to an entity in detached
		                         // state doesn't effect
		EntityTransaction t = entityManager.getTransaction();
		t.begin();
		entityManager.merge(pe); // moved from detached state to 
		                         // persistent state
		t.commit();
		entityManager.close();
		
	}

}
