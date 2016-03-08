package guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author lorabit
 * @since 16-2-26
 */
public class cache {

  private static ObjectMapper mapper = new ObjectMapper();

  private static LoadingCache<Integer, String> idBrandCache = CacheBuilder.newBuilder()
      .expireAfterAccess(10, TimeUnit.SECONDS)
      .recordStats()
      .build(new CacheLoader<Integer, String>() {
               @Override
               public String load(Integer key) throws Exception {
                 return String.valueOf(key);
               }
             }
      );


  @Test
  public void t() throws ExecutionException, IOException {
    idBrandCache.get(1);
    idBrandCache.get(1);
    idBrandCache.get(2);
    idBrandCache.get(3);
    idBrandCache.get(4);
    idBrandCache.get(5);
    CacheStats stats = idBrandCache.stats();
    System.out.println(stats.missCount());
    System.out.println(stats.hitCount());
    System.out.println(stats.averageLoadPenalty());
    System.out.println((int) (stats.hitRate() * 100));
    System.out.println((int) (stats.missRate() * 100));
    System.out.println(stats);
  }


}
